package com.dochiri.pragmatic.presentation.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.dochiri.pragmatic.application.auth.usecase.LoginUseCase;

public record LoginRequest(
        @Email
        String email,

        @Size(min = 8, max = 20)
        @NotBlank
        String password
) {
    public LoginUseCase.Input toInput() {
        return new LoginUseCase.Input(email, password);
    }
}