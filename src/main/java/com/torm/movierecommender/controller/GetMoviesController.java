package com.torm.movierecommender.controller;

import com.torm.movierecommender.dto.GetMoviesResponseItemDto;
import com.torm.movierecommender.entity.MovieEntity;
import com.torm.movierecommender.service.GetMoviesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class GetMoviesController {
    private final GetMoviesService getMoviesService;

    @GetMapping
    public Page<GetMoviesResponseItemDto> getMovies(@AuthenticationPrincipal Jwt jwt, @RequestParam(required = false) String search, @PageableDefault(sort = MovieEntity.Fields.movieId, direction = Sort.Direction.DESC) Pageable pageable) {
        return getMoviesService.getMovies(jwt, search, pageable);
    }
}