package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.AddMovieController.AddMovieRequestBody;
import com.torm.movierecommender.entities.MovieEntity;
import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.MovieRepository;
import com.torm.movierecommender.repositories.UserRepository;
import com.torm.movierecommender.utils.JsonUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddMovieService {
    private static final Map<String, String> CANONICAL_GENRES = Map.ofEntries(
            Map.entry("action", "Action"),
            Map.entry("adventure", "Adventure"),
            Map.entry("comedy", "Comedy"),
            Map.entry("drama", "Drama"),
            Map.entry("horror", "Horror"),
            Map.entry("thriller", "Thriller"),
            Map.entry("romance", "Romance"),
            Map.entry("sci-fi", "Sci-Fi"),
            Map.entry("fantasy", "Fantasy"),
            Map.entry("crime", "Crime"),
            Map.entry("mystery", "Mystery"),
            Map.entry("animation", "Animation")
    );

    private final UserRepository userRepository;
    private final EmbeddingModel embeddingModel;
    private final MovieRepository movieRepository;

    @Transactional
    public void addMovie(AddMovieRequestBody addMovieRequestBody, Jwt jwt) {
        String username = jwt.getSubject();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_UNAUTHORIZED_ERROR"));

        String title = addMovieRequestBody.title()
                .strip()
                .replaceAll("\\s+", " ");

        Short releaseYear = addMovieRequestBody.releaseYear();

        String directors = addMovieRequestBody.directors()
                .stream()
                .map(String::strip)
                .map(director -> director.replaceAll("\\s+", " "))
                .sorted()
                .collect(Collectors.joining(","));

        if (movieRepository.existsByTitleAndReleaseYearAndDirectorsAndUser(title, releaseYear, directors, user))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "MOVIE_CONFLICT_ERROR");

        String genres = addMovieRequestBody.genres()
                .stream()
                .map(String::strip)
                .map(String::toLowerCase)
                .map(CANONICAL_GENRES::get)
                .sorted()
                .collect(Collectors.joining(","));

        String plot = addMovieRequestBody.plot()
                .strip();

        MovieEntity movie = new MovieEntity();
        movie.setTitle(title);
        movie.setReleaseYear(releaseYear);
        movie.setDirectors(directors);
        movie.setGenres(genres);
        movie.setPlot(plot);

        float[] embeddings = embeddingModel.embed(plot);
        movie.setEmbeddings(JsonUtils.toJson(embeddings));

        movie.setRating(null);
        movie.setUser(user);

        movieRepository.save(movie);
    }
}