package com.torm.movierecommender.controllers;

import com.torm.movierecommender.services.RegisterService;
import com.torm.movierecommender.validation.ValidationGroupSequences.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    @NoArgsConstructor
    public static class RegisterRequestBody {
        @NotBlank(message = "Username cannot be blank.", groups = First.class)
        @Size(min = 3, message = "Username must be at least 3 characters long.", groups = Second.class)
        @Size(max = 32, message = "Username must be at most 32 characters long.", groups = Third.class)
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can contain only letters, numbers and underscores.", groups = Fourth.class)
        @Getter
        private String username;

        @NotBlank(message = "Email cannot be blank.", groups = First.class)
        @Email(message = "Email is invalid.", groups = Second.class)
        @Getter
        private String email;

        @NotBlank(message = "Password cannot be blank.", groups = First.class)
        @Size(min = 8, message = "Password must be at least 8 characters long.", groups = Second.class)
        @Size(max = 64, message = "Password must be at most 64 characters long.", groups = Third.class)
        @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter.", groups = Fourth.class)
        @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter.", groups = Fifth.class)
        @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one number.", groups = Sixth.class)
        @Pattern(regexp = ".*[~!@#$%^&*()_+`\\-=].*", message = "Password must contain at least one special character (~!@#$%^&*()_+`-=).", groups = Seventh.class)
        @Pattern(regexp = "^[A-Za-z0-9~!@#$%^&*()_+`\\-=]+$", message = "Password can contain only letters, numbers and special characters (~!@#$%^&*()_+`-=).", groups = Eighth.class)
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
    public RegisterResponseBody register(@RequestBody @Validated(ValidationGroupSequence1.class) RegisterRequestBody registerRequestBody) {
        return registerService.register(registerRequestBody);
    }
}