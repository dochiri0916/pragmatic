package com.example.pragmatic.application.user.dto;

import com.example.pragmatic.domain.user.User;

public record UserDetail(
        Long id,
        String email,
        String name,
        String role
) {
    public static UserDetail from(User user) {
        return new UserDetail(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }
}