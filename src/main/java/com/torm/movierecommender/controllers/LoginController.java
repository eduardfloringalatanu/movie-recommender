package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.LoginService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    public record LoginRequestBody(
            @NotBlank(message = "USERNAME_EMAIL_BLANK_ERROR")
            @Size(max = 254, message = "USERNAME_EMAIL_SIZE_ERROR")
            String usernameOrEmail,

            @NotBlank(message = "PASSWORD_BLANK_ERROR")
            @Size(max = 64, message = "PASSWORD_SIZE_ERROR")
            String password) {}
    public record LoginResponseBody(String accessToken, String refreshToken) {}

    @PostMapping("/login")
    public LoginResponseBody login(@RequestBody @Validated LoginRequestBody loginRequestBody) {
        return loginService.login(loginRequestBody);
    }
}