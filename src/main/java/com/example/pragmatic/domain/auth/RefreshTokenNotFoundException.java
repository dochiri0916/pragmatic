package com.example.pragmatic.domain.auth;

public class RefreshTokenNotFoundException extends RefreshTokenException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}