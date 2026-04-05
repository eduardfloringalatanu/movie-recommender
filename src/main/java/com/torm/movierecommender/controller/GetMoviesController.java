package com.torm.movierecommender.controller;

import com.torm.movierecommender.dto.GetMoviesResponseItemDto;
import com.torm.movierecommender.service.GetMoviesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class GetMoviesController {
    private final GetMoviesService getMoviesService;

    @GetMapping
    public List<GetMoviesResponseItemDto> getMovies(@AuthenticationPrincipal Jwt jwt) {
        return getMoviesService.getMovies(jwt);
    }
}