package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.RateMovieController.RateMovieRequestBody;
import com.torm.movierecommender.entities.MovieEntity;
import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.MovieRepository;
import com.torm.movierecommender.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RateMovieService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public void rateMovie(Long movieId, RateMovieRequestBody rateMovieRequestBody, Jwt jwt) {
        String username = jwt.getSubject();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_UNAUTHORIZED_ERROR"));

        MovieEntity movie = movieRepository.findByMovieIdAndUser(movieId, user)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "MOVIE_NOT_FOUND_ERROR"));

        movie.setRating(rateMovieRequestBody.rating());

        movieRepository.save(movie);
    }
}