package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.RecommendMoviesController.RecommendMoviesRequestBody;
import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.MovieRepository;
import com.torm.movierecommender.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class RecommendMoviesService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final OllamaChatModel ollamaChatModel;

    public Flux<String> recommendMovies(RecommendMoviesRequestBody recommendMoviesRequestBody, Jwt jwt) {
        String username = jwt.getSubject();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_UNAUTHORIZED_ERROR"));

        //

        String systemMessage = """
                You are a movie recommendation assistant.
                
                Task:
                - The user may provide:
                  1. A list of movies, each with its title, release year, directors, genres and plot.
                  2. An optional list of preferred genres.
                - For each movie:
                  - Explain why the user should watch it based solely on its plot.
                
                Rules:
                - Use only the plot for reasoning, even if other metadata is provided.
                - Focus only on story elements: themes, conflicts, emotions, intrigue or originality.
                - You may reference genres in the explanation, but only to support points that are clearly reflected in the plot.
                - If preferred genres are provided, prioritize highlighting matching elements when clearly supported by the plot.
                - Avoid making up information not present in the plot.
                - Keep explanations concise (2–4 sentences per movie).
                - Avoid revealing major plot twists or the ending.
                - Make each explanation engaging, persuasive and clearly focused on why the movie is worth watching.
                - Avoid repeating the same reasoning or phrasing across different movies.
                - Always include title, release year and directors exactly as provided.
                - Avoid using Oxford comma, em dashes, en dashes and hyphens.
                - Follow the format exactly as specified.
                - Start each explanation with a strong, specific hook based on the plot.
                
                Style:
                - Clear, friendly and accessible language.
                - Slightly enthusiastic, but realistic.
                
                Format:
                - Use a numbered list:
                  1. Movie title (Release year) directed by: Directors
                     Explanation.
                
                Example:
                1. The Shawshank Redemption (1994) directed by: Frank Darabont
                    ...
                
                2. The Godfather (1972) directed by: Francis Ford Coppola
                    ...
                
                3. The Dark Knight (2008) directed by: Christopher Nolan
                    ...
                """;
        String userMessage = "";

        return ollamaChatModel.stream(new Prompt(new SystemMessage(systemMessage), new UserMessage(userMessage)))
                .map(chatResponse -> chatResponse.getResult().getOutput().getText());
    }
}