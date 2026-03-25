package com.torm.movierecommender.controllers;

import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.MovieRepository;
import com.torm.movierecommender.repositories.RatingRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RateMovieController {
    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;

    public record RateMovieRequestBody(@Positive Long movie_id, @Min(1) @Max(10) Integer score) {}

    @PostMapping("/rate_movie")
    public void rateMovie(@RequestBody @Validated RateMovieRequestBody rateMovieRequestBody, @AuthenticationPrincipal UserEntity user) {

    }
}