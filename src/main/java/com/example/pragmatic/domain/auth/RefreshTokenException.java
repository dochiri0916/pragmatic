package com.example.pragmatic.domain.auth;

public abstract class RefreshTokenException extends RuntimeException {

    protected RefreshTokenException(String message) {
        super(message);
    }

    protected RefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}