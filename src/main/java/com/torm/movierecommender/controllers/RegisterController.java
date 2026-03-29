package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.RegisterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    public record RegisterRequestBody(
        @NotBlank(message = "USERNAME_BLANK_ERROR")
        @Size(min = 3, message = "USERNAME_SIZE_ERROR_1")
        @Size(max = 32, message = "USERNAME_SIZE_ERROR_2")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "USERNAME_PATTERN_ERROR")
        @Getter
        String username,

        @NotBlank(message = "EMAIL_BLANK_ERROR")
        @Email(message = "EMAIL_EMAIL_ERROR")
        @Getter
        String email,

        @NotBlank(message = "PASSWORD_BLANK_ERROR")
        @Size(min = 8, message = "PASSWORD_SIZE_ERROR_1")
        @Size(max = 64, message = "PASSWORD_SIZE_ERROR_2")
        @Pattern(regexp = ".*[a-z].*", message = "PASSWORD_PATTERN_ERROR_1")
        @Pattern(regexp = ".*[A-Z].*", message = "PASSWORD_PATTERN_ERROR_2")
        @Pattern(regexp = ".*[0-9].*", message = "PASSWORD_PATTERN_ERROR_3")
        @Pattern(regexp = ".*[~!@#$%^&*()_+`\\-=\\[\\]\\\\{}|;':\",./<>?].*", message = "PASSWORD_PATTERN_ERROR_4")
        @Pattern(regexp = "^[a-zA-Z0-9~!@#$%^&*()_+`\\-=\\[\\]\\\\{}|;':\",./<>?]+$", message = "PASSWORD_PATTERN_ERROR_5")
        @Getter @Setter
        String password) {}

    public record RegisterResponseBody(String accessToken, String refreshToken) {}

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseBody register(@RequestBody @Valid RegisterRequestBody registerRequestBody) {
        return registerService.register(registerRequestBody);
    }
}