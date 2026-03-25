package com.torm.movierecommender.controllers;

import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.UserRepository;
import com.torm.movierecommender.security.JwtUtil;
import com.torm.movierecommender.validation.ValidationGroupSequences.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LoginController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public record LoginRequestBody(
            @NotBlank(message = "Username/Email cannot be blank.", groups = First.class)
            @Size(max = 254, message = "Username/Email must be at most 254 characters long.", groups = Second.class)
            String usernameOrEmail,

            @NotBlank(message = "Password cannot be blank.", groups = First.class)
            @Size(max = 64, message = "Password must be at most 64 characters long.", groups = Second.class)
            String password) {}
    public record LoginResponseBody(String token) {}

    @PostMapping("/login")
    public LoginResponseBody login(@RequestBody @Validated(ValidationGroupSequence1.class) LoginRequestBody loginRequestBody) {
        UserEntity user = userRepository.findByUsername(loginRequestBody.usernameOrEmail())
                .or(() -> userRepository.findByEmail(loginRequestBody.usernameOrEmail()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/email or password."));

        if (!passwordEncoder.matches(loginRequestBody.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/email or password.");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return new LoginResponseBody(token);
    }
}