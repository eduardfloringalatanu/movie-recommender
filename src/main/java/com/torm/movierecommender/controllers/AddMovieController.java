package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.AddMovieService;
import com.torm.movierecommender.validation.MaxYear;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class AddMovieController {
    private final AddMovieService addMovieService;

    public record AddMovieRequestBody(
            @NotBlank(message = "TITLE_BLANK_ERROR")
            String title,

            @NotNull(message = "RELEASE_YEAR_NULL_ERROR")
            @Min(value = 1888, message = "RELEASE_YEAR_MIN_ERROR")
            @MaxYear(message = "RELEASE_YEAR_MAX_ERROR")
            Short releaseYear,

            @NotEmpty(message = "DIRECTORS_EMPTY_ERROR")
            Set<@NotBlank(message = "DIRECTORS_ITEMS_BLANK_ERROR") String> directors,

            @NotEmpty(message = "GENRES_EMPTY_ERROR")
            Set<@NotBlank(message = "GENRES_ITEMS_BLANK_ERROR") String> genres,

            @NotBlank(message = "PLOT_BLANK_ERROR")
            String plot) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addMovie(@RequestBody @Valid AddMovieRequestBody addMovieRequestBody, @AuthenticationPrincipal Jwt jwt) {
        addMovieService.addMovie(addMovieRequestBody, jwt);
    }
}