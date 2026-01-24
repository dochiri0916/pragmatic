package com.example.pragmatic.domain.auth;

public class RefreshTokenExpiredException extends RefreshTokenException {
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}