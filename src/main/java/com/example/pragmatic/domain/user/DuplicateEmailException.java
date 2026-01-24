package com.example.pragmatic.domain.user;

public class DuplicateEmailException extends UserException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}