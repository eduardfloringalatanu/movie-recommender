package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.RateMovieController.RateMovieRequestBody;
import com.torm.movierecommender.entities.MovieEntity;
import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.MovieRepository;
import com.torm.movierecommender.repositories.UserRepository;
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

    public void rateMovie(RateMovieRequestBody rateMovieRequestBody, Jwt jwt) {
        String username = jwt.getSubject();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found."));

        MovieEntity movie = movieRepository.findByMovieIdAndUser(rateMovieRequestBody.movie_id(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found."));

        //
    }
}