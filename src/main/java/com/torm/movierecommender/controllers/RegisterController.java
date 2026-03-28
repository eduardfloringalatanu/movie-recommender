package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.RegisterService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    @NoArgsConstructor
    public static class RegisterRequestBody {
        @NotBlank(message = "USERNAME_BLANK_ERROR")
        @Size(min = 3, message = "USERNAME_SIZE_ERROR_1")
        @Size(max = 32, message = "USERNAME_SIZE_ERROR_2")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "USERNAME_PATTERN_ERROR")
        @Getter
        private String username;

        @NotBlank(message = "EMAIL_BLANK_ERROR")
        @Email(message = "EMAIL_EMAIL_ERROR")
        @Getter
        private String email;

        @NotBlank(message = "PASSWORD_BLANK_ERROR")
        @Size(min = 8, message = "PASSWORD_SIZE_ERROR_1")
        @Size(max = 64, message = "PASSWORD_SIZE_ERROR_2")
        @Pattern(regexp = ".*[a-z].*", message = "PASSWORD_PATTERN_ERROR_1")
        @Pattern(regexp = ".*[A-Z].*", message = "PASSWORD_PATTERN_ERROR_2")
        @Pattern(regexp = ".*[0-9].*", message = "PASSWORD_PATTERN_ERROR_3")
        @Pattern(regexp = ".*[~!@#$%^&*()_+`\\-=].*", message = "PASSWORD_PATTERN_ERROR_4")
        @Pattern(regexp = "^[A-Za-z0-9~!@#$%^&*()_+`\\-=]+$", message = "PASSWORD_PATTERN_ERROR_5")
        @Getter @Setter
        private String password;

        public RegisterRequestBody(String username, String email, String password) {
            this.setUsername(username);
            this.setEmail(email);
            this.setPassword(password);
        }

        public void setUsername(String username) {
            this.username = username != null ? username.strip() : null;
        }

        public void setEmail(String email) {
            this.email = email != null ? email.strip() : null;
        }
    }

    public record RegisterResponseBody(String accessToken, String refreshToken) {}

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseBody register(@RequestBody @Validated RegisterRequestBody registerRequestBody) {
        return registerService.register(registerRequestBody);
    }
}