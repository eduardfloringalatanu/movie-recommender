package com.torm.movierecommender.service;

import com.torm.movierecommender.dto.GetMoviesResponseItemDto;
import com.torm.movierecommender.entity.UserEntity;
import com.torm.movierecommender.exception.ErrorCode;
import com.torm.movierecommender.exception.ResponseStatusException2;
import com.torm.movierecommender.repository.MovieRepository;
import com.torm.movierecommender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMoviesService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public List<GetMoviesResponseItemDto> getMovies(Jwt jwt) {
        String username = jwt.getSubject();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException2(HttpStatus.UNAUTHORIZED, ErrorCode.USER_UNAUTHORIZED_ERROR));

        return movieRepository.findByUser(user)
                .stream()
                .map(movie -> {
                    Set<String> directors = Set.of(movie.getDirectors().split(","));
                    Set<String> genres = Set.of(movie.getGenres().split(","));

                    return new GetMoviesResponseItemDto(
                            movie.getMovieId(),
                            movie.getTitle(),
                            movie.getReleaseYear(),
                            directors,
                            genres,
                            movie.getPlot(),
                            movie.getRating()
                    );
                })
                .toList();
    }
}