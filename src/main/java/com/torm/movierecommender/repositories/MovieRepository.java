package com.torm.movierecommender.repositories;

import com.torm.movierecommender.entities.MovieEntity;
import com.torm.movierecommender.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    Optional<MovieEntity> findByMovieIdAndUser(Long movieId, UserEntity user);
    List<MovieEntity> findByRatingGreaterThanEqualAndUser(Short rating, UserEntity user);
}