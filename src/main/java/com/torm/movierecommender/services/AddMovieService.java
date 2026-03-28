package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.AddMovieController.AddMovieRequestBody;
import com.torm.movierecommender.entities.MovieEntity;
import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.MovieRepository;
import com.torm.movierecommender.repositories.UserRepository;
import com.torm.movierecommender.utils.EmbeddingsUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_UNAUTHORIZED_ERROR"));

        MovieEntity movie = new MovieEntity();
        movie.setTitle(addMovieRequestBody.title());
        movie.setReleaseYear(addMovieRequestBody.releaseYear());
        movie.setDirectors(String.join(",", addMovieRequestBody.directors()));
        movie.setGenres(String.join(",", addMovieRequestBody.genres()));
        movie.setPlot(addMovieRequestBody.plot());

        float[] embeddings = embeddingModel.embed(addMovieRequestBody.plot());
        movie.setEmbeddings(EmbeddingsUtils.embeddingsToJson(embeddings));

        movie.setRating(null);
        movie.setUser(user);

        movieRepository.save(movie);
    }
}