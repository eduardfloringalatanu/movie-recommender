package com.torm.movierecommender.services;

import com.torm.movierecommender.controllers.LoginController.LoginRequestBody;
import com.torm.movierecommender.controllers.LoginController.LoginResponseBody;
import com.torm.movierecommender.entities.UserEntity;
import com.torm.movierecommender.repositories.UserRepository;
import com.torm.movierecommender.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public LoginResponseBody login(LoginRequestBody loginRequestBody) {
        UserEntity user = userRepository.findByUsername(loginRequestBody.usernameOrEmail())
                .or(() -> userRepository.findByEmail(loginRequestBody.usernameOrEmail()))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "CREDENTIALS_INVALID_ERROR"));

        if (!passwordEncoder.matches(loginRequestBody.password(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "CREDENTIALS_INVALID_ERROR");

        String accessToken = tokenService.generateAccessToken(user.getUsername());

        String refreshToken = tokenService.generateRefreshToken(user.getUserId());

        return new LoginResponseBody(accessToken, refreshToken);
    }
}