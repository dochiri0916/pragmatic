package com.example.pragmatic.presatation.auth.response;

import com.example.pragmatic.domain.user.User;

public record AuthResponse(
        Long userId,
        String role,
        String accessToken,
        String refreshToken
) {
    public static AuthResponse from(
            final User user,
            final String accessToken,
            final String refreshToken
    ) {
        return new AuthResponse(
                user.getId(),
                user.getRole().name(),
                accessToken,
                refreshToken
        );
    }
}