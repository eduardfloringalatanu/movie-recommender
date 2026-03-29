package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.RefreshAccessTokenController.RefreshTokenRequestBody;
import com.torm.movierecommender.controllers.RefreshAccessTokenController.RefreshTokenResponseBody;
import com.torm.movierecommender.entities.RefreshTokenEntity;
import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.RefreshTokenRepository;
import com.torm.movierecommender.security.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshAccessTokenService {
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshTokenResponseBody refreshAccessToken(RefreshTokenRequestBody refreshTokenRequestBody) {
        String hashedRefreshToken = tokenService.hashRefreshToken(refreshTokenRequestBody.refreshToken());

        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(hashedRefreshToken)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_INVALID_ERROR"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_INVALID_ERROR");
        }

        UserEntity user = refreshToken.getUser();

        if (refreshToken.getRevokedAt() != null) {
            if (Instant.now().isAfter(refreshToken.getRevokedAt().plusSeconds(30))) {
                refreshTokenRepository.deleteByUser(user);

                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_INVALID_ERROR");
            }
        } else {
            refreshToken.setRevokedAt(Instant.now());
            refreshTokenRepository.save(refreshToken);
        }

        String newAccessToken = tokenService.generateAccessToken(user.getUsername());
        String newRefreshToken = tokenService.generateRefreshToken(user.getUserId());

        return new RefreshTokenResponseBody(newAccessToken, newRefreshToken);
    }
}