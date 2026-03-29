package com.torm.movierecommender.repositories;

import com.torm.movierecommender.entities.RefreshTokenEntity;
import com.torm.movierecommender.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity WHERE expiryDate < :now")
    void deleteAllExpiredSince(Instant now);

    @Modifying
    void deleteByUser(UserEntity user);
}