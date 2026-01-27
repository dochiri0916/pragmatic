package com.example.pragmatic.domain.user;

public class UserNotActiveException extends UserException {
    public UserNotActiveException(String message) {
        super(message);
    }
}