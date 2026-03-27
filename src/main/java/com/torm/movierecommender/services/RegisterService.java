package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.RegisterController.RegisterRequestBody;
import com.torm.movierecommender.controllers.RegisterController.RegisterResponseBody;
import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.UserRepository;
import com.torm.movierecommender.security.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public RegisterResponseBody register(RegisterRequestBody registerRequestBody) {
        if (userRepository.findByUsername(registerRequestBody.getUsername()).isPresent() ||
                userRepository.findByEmail(registerRequestBody.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username/Email already exists.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerRequestBody.getUsername());
        user.setEmail(registerRequestBody.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestBody.getPassword()));

        userRepository.save(user);

        String accessToken = tokenService.generateAccessToken(user.getUsername());

        String refreshToken = tokenService.generateRefreshToken(user.getUserId());

        return new RegisterResponseBody(accessToken, refreshToken);
    }
}