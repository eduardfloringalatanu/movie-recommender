package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.LogoutController.LogoutRequestBody;
import com.torm.movierecommender.repositories.RefreshTokenRepository;
import com.torm.movierecommender.security.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void logout(LogoutRequestBody logoutRequestBody) {
        refreshTokenRepository.findByToken(tokenService.hashRefreshToken(logoutRequestBody.refreshToken()))
                .ifPresent(refreshTokenRepository::delete);
    }
}