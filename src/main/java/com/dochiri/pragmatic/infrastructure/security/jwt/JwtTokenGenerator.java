package com.dochiri.pragmatic.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private final JwtProvider jwtProvider;

    public JwtTokenResult generateToken(Long userId, String role) {
        String accessToken = jwtProvider.generateAccessToken(userId, role);
        String refreshToken = jwtProvider.generateRefreshToken(userId, role);

        LocalDateTime refreshExpiresAt = jwtProvider.refreshTokenExpiresAt();

        return new JwtTokenResult(accessToken, refreshToken, refreshExpiresAt);
    }

    public String generateAccessToken(Long userId, String role) {
        return jwtProvider.generateAccessToken(userId, role);
    }

}