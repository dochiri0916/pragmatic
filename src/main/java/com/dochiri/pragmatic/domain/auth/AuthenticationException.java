package com.dochiri.pragmatic.domain.auth;

public abstract class AuthenticationException extends RuntimeException {
    protected AuthenticationException(String message) {
        super(message);
    }
}