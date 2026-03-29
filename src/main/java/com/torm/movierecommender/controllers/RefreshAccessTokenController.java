package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.RefreshAccessTokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RefreshAccessTokenController {
    private final RefreshAccessTokenService refreshAccessTokenService;

    public record RefreshTokenRequestBody(
            @NotBlank(message = "REFRESH_TOKEN_BLANK_ERROR")
            String refreshToken) {}
    public record RefreshTokenResponseBody(String accessToken, String refreshToken) {}

    @PostMapping("/refresh")
    public RefreshTokenResponseBody refreshAccessToken(@RequestBody @Valid RefreshTokenRequestBody refreshTokenRequestBody) {
        return refreshAccessTokenService.refreshAccessToken(refreshTokenRequestBody);
    }
}