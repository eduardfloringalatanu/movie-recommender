package com.torm.movierecommender.repository;

import com.torm.movierecommender.entity.MovieEntity;
import com.torm.movierecommender.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    Optional<MovieEntity> findByMovieIdAndUser(Long movieId, UserEntity user);
    List<MovieEntity> findByRatingGreaterThanEqualAndUser(Short rating, UserEntity user);
    List<MovieEntity> findByRatingGreaterThanEqualAndUserOrderByRatingDesc(Short rating, UserEntity user);
    List<MovieEntity> findByRatingIsNullAndUser(UserEntity user);
    List<MovieEntity> findByRatingIsNullAndReleaseYearGreaterThanEqualAndUser(Short releaseYear, UserEntity user);
    boolean existsByTitleAndReleaseYearAndDirectorsAndUser(String title, Short releaseYear, String directors, UserEntity user);
    Page<MovieEntity> findByUser(UserEntity user, Pageable pageable);

    @Query("SELECT m FROM MovieEntity m WHERE m.user = :user AND LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<MovieEntity> findMoviesByUserAndTitle(
            @Param("user") UserEntity user,
            @Param("search") String search,
            Pageable pageable
    );
}