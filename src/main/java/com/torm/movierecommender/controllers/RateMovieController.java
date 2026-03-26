package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.RateMovieService;
import com.torm.movierecommender.validation.ValidationGroupSequences.First;
import com.torm.movierecommender.validation.ValidationGroupSequences.Second;
import com.torm.movierecommender.validation.ValidationGroupSequences.ValidationGroupSequence1;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RateMovieController {
    private final RateMovieService rateMovieService;

    public record RateMovieRequestBody(
            @Positive(message = "Movie ID cannot be negative.", groups = First.class)
            Long movie_id,

            @Range(min = 1, max = 10, message = "Score must be between 1 and 10.", groups = Second.class)
            Integer score) {}

    @PostMapping("/rate_movie")
    public void rateMovie(@RequestBody @Validated(ValidationGroupSequence1.class) RateMovieRequestBody rateMovieRequestBody, @AuthenticationPrincipal Jwt jwt) {
        rateMovieService.rateMovie(rateMovieRequestBody, jwt);
    }
}