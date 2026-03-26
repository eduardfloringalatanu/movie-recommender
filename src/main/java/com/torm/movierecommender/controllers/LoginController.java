package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.LoginService;
import com.torm.movierecommender.validation.ValidationGroupSequences.First;
import com.torm.movierecommender.validation.ValidationGroupSequences.Second;
import com.torm.movierecommender.validation.ValidationGroupSequences.ValidationGroupSequence1;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    public record LoginRequestBody(
            @NotBlank(message = "Username/Email cannot be blank.", groups = First.class)
            @Size(max = 254, message = "Username/Email must be at most 254 characters long.", groups = Second.class)
            String usernameOrEmail,

            @NotBlank(message = "Password cannot be blank.", groups = First.class)
            @Size(max = 64, message = "Password must be at most 64 characters long.", groups = Second.class)
            String password) {}
    public record LoginResponseBody(String accessToken, String refreshToken) {}

    @PostMapping("/login")
    public LoginResponseBody login(@RequestBody @Validated(ValidationGroupSequence1.class) LoginRequestBody loginRequestBody) {
        return loginService.login(loginRequestBody);
    }
}