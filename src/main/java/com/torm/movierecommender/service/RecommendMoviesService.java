package com.torm.movierecommender.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.torm.movierecommender.dto.RecommendMoviesRequestDto;
import com.torm.movierecommender.entity.MovieEntity;
import com.torm.movierecommender.entity.UserEntity;
import com.torm.movierecommender.exception.ErrorCode;
import com.torm.movierecommender.exception.ResponseStatusException2;
import com.torm.movierecommender.repository.MovieRepository;
import com.torm.movierecommender.repository.UserRepository;
import com.torm.movierecommender.util.GenreUtils;
import com.torm.movierecommender.util.MathUtils;
import com.torm.movierecommender.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendMoviesService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final OllamaChatModel ollamaChatModel;

    private float[] buildCentroid(UserEntity user) {
        List<MovieEntity> movies = movieRepository.findByRatingGreaterThanEqualAndUserOrderByRatingDesc((short)5, user);

        float[] embeddings;
        float[] centroid = null;

        float weight, totalWeight = 0;

        for (MovieEntity movie : movies) {
            embeddings = JsonUtils.fromJson(movie.getEmbeddings(), new TypeReference<>() {});

            weight = movie.getRating().floatValue();
            totalWeight += weight;

            if (centroid == null)
                centroid = new float[embeddings.length];

            for (int i = 0; i < centroid.length; i++)
                centroid[i] += embeddings[i] * weight;
        }

        if (centroid != null && totalWeight > 0) {
            for (int i = 0; i < centroid.length; ++i)
                centroid[i] /= totalWeight;
        }

        return centroid;
    }

    private Set<String> getTop3PreferredGenres(UserEntity user) {
        List<MovieEntity> movies = movieRepository.findByRatingGreaterThanEqualAndUser((short)5, user);

        if (movies.isEmpty())
            return Collections.emptySet();

        return movies.stream()
                .map(MovieEntity::getGenres)
                .flatMap(genre -> Arrays.stream(genre.split(",")))
                .collect(Collectors.groupingBy(genre -> genre, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public Flux<String> recommendMovies(RecommendMoviesRequestDto recommendMoviesRequestDto, Jwt jwt) {
        String username = jwt.getSubject();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException2(HttpStatus.UNAUTHORIZED, ErrorCode.USER_UNAUTHORIZED_ERROR));

        String systemMessage = """
                You are a movie recommendation assistant.
                
                Context:
                You will receive a JSON object containing:
                1. 'preferredGenres': A list of the user's top favorite movie genres.
                2. 'movieRecommendationCandidateList': A list of top candidate movies. Each movie contains its 'title', 'releaseYear', 'directors', 'genres', 'plot' and a mathematically calculated 'matchingScore' (ranging from 0.0 to 1.0).
                
                Task:
                Write a highly personalized, engaging and persuasive pitch for why the user should watch each movie, acting as the bridge between our recommendation algorithm and the human user.
                
                Rules:
                - SYNTHESIZE THE DATA: Build your argument by combining the movie's 'plot', its 'genres' and the user's 'preferredGenres'.
                - USE THE SCORE WISELY: Translate the 'matchingScore' into a human readable format (like "a 92% match") and integrate it naturally INSIDE the explanation paragraph. Do not explain the math behind the score, just use it to show enthusiasm.
                - CONNECT PLOT TO PREFERENCES: Explicitly tie elements of the plot to the user's 'preferredGenres' to show WHY it matches.
                - STRICT LIMITS: Keep explanations concise (exactly 2 to 4 sentences per movie).
                - NO SPOILERS: Focus on themes, conflicts and intrigue. Never reveal twists or the ending.
                - UNIQUE HOOKS: Start every explanation with a strong, specific hook based on the plot. Avoid repeating phrasing across movies.
                - GRAMMAR CONSTRAINTS: Avoid using the Oxford comma, em dashes and en dashes.
                - STRUCTURE: Your response should contain only movie recommendations, not any other explanations.
                
                Format:
                Follow this exact structure for each movie:
                1. Title (Release Year) directed by: Directors
                   Explanation.
                
                Example:
                1. The Shawshank Redemption (1994) directed by: Frank Darabont
                   Since you enjoy deep Drama and gripping narratives this story of hope is a perfect 98% match for your tastes. It follows a banker wrongfully convicted of murder who must navigate the harsh realities of prison life while forging an unexpected friendship. The brilliant character development will keep you completely captivated from start to finish.
                """;

        float[] centroid = buildCentroid(user);
        Set<String> preferredGenres = getTop3PreferredGenres(user);

        Set<String> excludedGenres = Optional.ofNullable(recommendMoviesRequestDto.excludedGenres())
                .orElseGet(Collections::emptySet)
                .stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .map(GenreUtils.GENRE_DICTIONARY::get)
                .collect(Collectors.toSet());

        boolean hasExcludedGenres = !excludedGenres.isEmpty();

        Short minReleaseYear = recommendMoviesRequestDto.minReleaseYear();

        int currentYear = LocalDate.now().getYear();
        float midYear = (1888 + currentYear) / 2.0f;
        float scale = (currentYear - 1888) / 4.0f;

        List<MovieEntity> movies = minReleaseYear == null ?
                movieRepository.findByRatingIsNullAndUser(user) :
                movieRepository.findByRatingIsNullAndReleaseYearGreaterThanEqualAndUser(minReleaseYear, user);

        record MovieRecommendationCandidate(
                String title,
                Short releaseYear,
                String directors,
                String genres,
                String plot,
                float matchingScore
        ) {}

        List<MovieRecommendationCandidate> movieRecommendationCandidateList = movies.stream()
                .filter(movie -> {
                    if (!hasExcludedGenres)
                        return true;

                    for (String genre : movie.getGenres().split(",")) {
                        if (excludedGenres.contains(genre))
                            return false;
                    }

                    return true;
                })
                .map(movie -> {
                    float[] embeddings = JsonUtils.fromJson(movie.getEmbeddings(), new TypeReference<>() {});
                    float cosineSimilarityFactor = centroid == null ?
                            0.0f :
                            MathUtils.cosineSimilarity(centroid, embeddings);

                    cosineSimilarityFactor = Math.max(0.0f, cosineSimilarityFactor);

                    float genresCountFactor = 0.0f;

                    if (!preferredGenres.isEmpty()) {
                        int genresCount = 0;

                        for (String genre : movie.getGenres().split(",")) {
                            if (preferredGenres.contains(genre))
                                ++genresCount;
                        }

                        genresCountFactor = Math.min(1.0f, (float)genresCount / preferredGenres.size());
                    }

                    float releaseYearFactor = ((float)Math.tanh((movie.getReleaseYear() - midYear) / scale) + 1.0f) / 2.0f;

                    float matchingScore = (0.6f * cosineSimilarityFactor) +
                            (0.3f * genresCountFactor) +
                            (0.1f * releaseYearFactor);

                    return new MovieRecommendationCandidate(
                            movie.getTitle(),
                            movie.getReleaseYear(),
                            movie.getDirectors(),
                            movie.getGenres(),
                            movie.getPlot(),
                            matchingScore
                    );
                })
                .sorted(Comparator.comparing(MovieRecommendationCandidate::matchingScore).reversed())
                .limit(10)
                .toList();

        record MovieRecommendationUserMessage(
                Set<String> preferredGenres,
                List<MovieRecommendationCandidate> movieRecommendationCandidateList
        ) {}

        String userMessage = JsonUtils.toJson(new MovieRecommendationUserMessage(preferredGenres, movieRecommendationCandidateList));

        return ollamaChatModel.stream(new Prompt(new SystemMessage(systemMessage), new UserMessage(userMessage)))
                .map(chatResponse ->
                        chatResponse.getResult()
                                .getOutput()
                                .getText());
    }
}