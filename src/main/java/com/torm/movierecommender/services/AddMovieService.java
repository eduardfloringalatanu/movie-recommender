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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddMovieService {
    private final UserRepository userRepository;
    private final EmbeddingModel embeddingModel;
    private final MovieRepository movieRepository;

    @Transactional
    public void addMovie(AddMovieRequestBody addMovieRequestBody, Jwt jwt) {
        String username = jwt.getSubject();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_UNAUTHORIZED_ERROR"));

        String directors = addMovieRequestBody.directors()
                .stream()
                .sorted()
                .collect(Collectors.joining(","));

        if (movieRepository.existsByTitleAndDirectorsAndUser(addMovieRequestBody.title(), directors, user))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "MOVIE_CONFLICT_ERROR");

        MovieEntity movie = new MovieEntity();
        movie.setTitle(addMovieRequestBody.title());
        movie.setReleaseYear(addMovieRequestBody.releaseYear());
        movie.setDirectors(directors);
        movie.setGenres(String.join(",", addMovieRequestBody.genres()));
        movie.setPlot(addMovieRequestBody.plot());

        float[] embeddings = embeddingModel.embed(addMovieRequestBody.plot());
        movie.setEmbeddings(JsonUtils.toJson(embeddings));

        movie.setRating(null);
        movie.setUser(user);

        movieRepository.save(movie);
    }
}