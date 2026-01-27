package com.example.pragmatic.domain.auth;

public class InvalidRefreshTokenException extends RefreshTokenException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}