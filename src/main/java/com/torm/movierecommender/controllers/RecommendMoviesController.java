package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.RecommendMoviesService;
import com.torm.movierecommender.validation.MaxYear;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.util.Set;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class RecommendMoviesController {
    private final RecommendMoviesService recommendMoviesService;

    public record RecommendMoviesRequestBody(
            Set<@NotBlank(message = "EXCLUDED_GENRES_ITEMS_BLANK_ERROR") String> excludedGenres,

            @NotNull(message = "MIN_RELEASE_YEAR_NULL_ERROR")
            @Min(value = 1888, message = "MIN_RELEASE_YEAR_MIN_ERROR")
            @MaxYear(message = "MIN_RELEASE_YEAR_MAX_ERROR")
            Short minReleaseYear) {}

    @PostMapping(value = "/recommend", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> recommendMovies(@RequestBody @Validated RecommendMoviesRequestBody recommendMoviesRequestBody, @AuthenticationPrincipal Jwt jwt) {
        return recommendMoviesService.recommendMovies(recommendMoviesRequestBody, jwt);
    }
}