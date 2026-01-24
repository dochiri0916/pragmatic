package com.example.pragmatic.application.auth.facade;

import com.example.pragmatic.application.auth.command.AuthenticationService;
import com.example.pragmatic.application.auth.command.TokenService;
import com.example.pragmatic.domain.user.User;
import com.example.pragmatic.infrastructure.security.jwt.JwtProvider;
import com.example.pragmatic.presatation.auth.response.AuthResponse;
import com.example.pragmatic.presatation.auth.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;

    public AuthResponse login(final LoginRequest request) {
        User user = authenticationService.authenticate(request);

        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getRole().name());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId(), user.getRole().name());

        tokenService.issueRefreshToken(
                refreshToken,
                user.getId(),
                LocalDateTime.now().plusDays(14)
        );

        return AuthResponse.from(
                user,
                accessToken,
                refreshToken
        );
    }

}