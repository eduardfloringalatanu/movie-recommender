package com.torm.movierecommender.dto;

import java.util.Set;

public record GetMoviesResponseItemDto(
        Long movieId,

        String title,

        Short releaseYear,

        Set<String> directors,

        Set<String> genres,

        String plot,

        Short rating
) {}