package com.example.pragmatic.presentation.auth.response;

import com.example.pragmatic.domain.user.User;

public record AuthResponse(
        Long userId,
        String role,
        String accessToken,
        String refreshToken
) {
    public static AuthResponse from(
            User user,
            String accessToken,
            String refreshToken
    ) {
        return new AuthResponse(
                user.getId(),
                user.getRole().name(),
                accessToken,
                refreshToken
        );
    }
}