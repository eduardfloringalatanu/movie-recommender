package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.LogoutService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LogoutController {
    private final LogoutService logoutService;

    public record LogoutRequestBody(
            @NotBlank(message = "REFRESH_TOKEN_BLANK_ERROR")
            String refreshToken) {}

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody @Validated LogoutRequestBody logoutRequestBody, @AuthenticationPrincipal Jwt jwt) {
        logoutService.logout(logoutRequestBody, jwt);
    }
}