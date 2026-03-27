package com.torm.movierecommender.controllers;

import com.torm.movierecommender.repositories.RefreshTokenRepository;
import com.torm.movierecommender.security.TokenService;
import com.torm.movierecommender.services.LogoutService;
import com.torm.movierecommender.validation.ValidationGroupSequences.First;
import com.torm.movierecommender.validation.ValidationGroupSequences.ValidationGroupSequence1;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LogoutController {
    private final LogoutService logoutService;

    public record LogoutRequestBody(
            @NotBlank(message = "Refresh token cannot be blank.", groups = First.class)
            String refreshToken) {}

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody @Validated(ValidationGroupSequence1.class) LogoutRequestBody logoutRequestBody, @AuthenticationPrincipal Jwt jwt) {
        logoutService.logout(logoutRequestBody, jwt);
    }
}