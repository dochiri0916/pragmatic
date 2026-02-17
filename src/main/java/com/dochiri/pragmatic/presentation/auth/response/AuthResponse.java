package com.dochiri.pragmatic.presentation.auth.response;

import com.dochiri.pragmatic.domain.user.User;

public record AuthResponse(
        Long userId,
        String role,
        String accessToken
) {
    public static AuthResponse from(
            User user,
            String accessToken
    ) {
        return new AuthResponse(
                user.getId(),
                user.getRole().name(),
                accessToken
        );
    }
}
