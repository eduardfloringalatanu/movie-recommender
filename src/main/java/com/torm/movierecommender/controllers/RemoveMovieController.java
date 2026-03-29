package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.RemoveMovieService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Validated
public class RemoveMovieController {
    private final RemoveMovieService removeMovieService;

    @DeleteMapping("/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMovie(@PathVariable @Positive(message = "MOVIE_ID_POSITIVE_ERROR") Long movieId, @AuthenticationPrincipal Jwt jwt) {
        removeMovieService.removeMovie(movieId, jwt);
    }
}