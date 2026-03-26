package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.RefreshAccessTokenService;
import com.torm.movierecommender.validation.ValidationGroupSequences.First;
import com.torm.movierecommender.validation.ValidationGroupSequences.ValidationGroupSequence1;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RefreshAccessTokenController {
    private final RefreshAccessTokenService refreshAccessTokenService;

    public record RefreshTokenRequestBody(
            @NotBlank(message = "Refresh token cannot be blank.", groups = First.class)
            String refreshToken) {}
    public record RefreshTokenResponseBody(String accessToken, String refreshToken) {}

    @PostMapping("/refresh_access_token")
    public RefreshTokenResponseBody refreshAccessToken(@RequestBody @Validated(ValidationGroupSequence1.class) RefreshTokenRequestBody refreshTokenRequestBody) {
        return refreshAccessTokenService.refreshAccessToken(refreshTokenRequestBody);
    }
}