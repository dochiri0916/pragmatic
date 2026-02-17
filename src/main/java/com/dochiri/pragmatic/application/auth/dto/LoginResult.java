package com.dochiri.pragmatic.application.auth.dto;

import com.dochiri.pragmatic.domain.user.User;

public record LoginResult(
        User user,
        String accessToken,
        String refreshToken
) {
    public static LoginResult from(User user, String accessToken, String refreshToken) {
        return new LoginResult(user, accessToken, refreshToken);
    }
}