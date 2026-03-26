package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.RefreshAccessTokenController.RefreshTokenRequestBody;
import com.torm.movierecommender.controllers.RefreshAccessTokenController.RefreshTokenResponseBody;
import com.torm.movierecommender.entities.RefreshTokenEntity;
import com.torm.movierecommender.repositories.RefreshTokenRepository;
import com.torm.movierecommender.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RefreshAccessTokenService {
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenResponseBody refreshAccessToken(RefreshTokenRequestBody refreshTokenRequestBody) {
        String hashedRefreshToken = tokenService.hashRefreshToken(refreshTokenRequestBody.refreshToken());

        return refreshTokenRepository.findByToken(hashedRefreshToken)
                .map(tokenService::verifyExpirationDate)
                .map(RefreshTokenEntity::getUser)
                .map(user -> {
                    String accessToken = tokenService.generateAccessToken(user.getUsername());

                    return new RefreshTokenResponseBody(accessToken, refreshTokenRequestBody.refreshToken());
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is invalid."));
    }
}