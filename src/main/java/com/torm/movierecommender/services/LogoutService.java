package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.LogoutController.LogoutRequestBody;
import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.RefreshTokenRepository;
import com.torm.movierecommender.repositories.UserRepository;
import com.torm.movierecommender.security.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void logout(LogoutRequestBody logoutRequestBody, Jwt jwt) {
        String username = jwt.getSubject();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_UNAUTHORIZED_ERROR"));

        refreshTokenRepository.findByToken(tokenService.hashRefreshToken(logoutRequestBody.refreshToken()))
                .ifPresent(token -> {
                    if (token.getUser().getUserId().equals(user.getUserId()))
                        refreshTokenRepository.delete(token);
                });
    }
}