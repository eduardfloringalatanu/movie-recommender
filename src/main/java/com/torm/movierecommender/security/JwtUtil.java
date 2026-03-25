package com.torm.movierecommender.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final SecretKey secretKey;

    @Value("${jwt.secret-key-expiration}")
    private long secretKeyExpiration;

    public String generateToken(String identifier) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(secretKeyExpiration)))
                .signWith(secretKey)
                .compact();
    }
}