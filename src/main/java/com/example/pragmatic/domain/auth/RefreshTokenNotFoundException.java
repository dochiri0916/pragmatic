package com.example.pragmatic.domain.auth;

public class RefreshTokenNotFoundException extends RefreshTokenException {
    public RefreshTokenNotFoundException() {
        super("해당 토큰을 찾을 수 없습니다.");
    }
}