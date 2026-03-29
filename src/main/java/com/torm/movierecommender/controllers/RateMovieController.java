package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.RateMovieService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Validated
public class RateMovieController {
    private final RateMovieService rateMovieService;

    public record RateMovieRequestBody(
            @Range(min = 1, max = 10, message = "RATING_RANGE_ERROR")
            Short rating) {}

    @PutMapping("/{movieId}/rating")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rateMovie(@PathVariable @Positive(message = "MOVIE_ID_POSITIVE_ERROR") Long movieId, @RequestBody @Valid RateMovieRequestBody rateMovieRequestBody, @AuthenticationPrincipal Jwt jwt) {
        rateMovieService.rateMovie(movieId, rateMovieRequestBody, jwt);
    }
}