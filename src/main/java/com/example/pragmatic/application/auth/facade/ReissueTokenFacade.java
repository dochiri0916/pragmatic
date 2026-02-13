package com.example.pragmatic.application.auth.facade;

import com.example.pragmatic.application.auth.query.RefreshTokenLoader;
import com.example.pragmatic.application.user.query.UserLoader;
import com.example.pragmatic.domain.auth.InvalidRefreshTokenException;
import com.example.pragmatic.domain.auth.RefreshToken;
import com.example.pragmatic.domain.user.User;
import com.example.pragmatic.infrastructure.security.jwt.JwtTokenGenerator;
import com.example.pragmatic.infrastructure.security.jwt.RefreshTokenVerifier;
import com.example.pragmatic.presentation.auth.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReissueTokenFacade {

    private final RefreshTokenVerifier refreshTokenVerifier;
    private final RefreshTokenLoader refreshTokenLoader;
    private final UserLoader userLoader;
    private final JwtTokenGenerator jwtTokenGenerator;

    public AuthResponse reissue(final String refreshTokenValue) {
        Long userId = refreshTokenVerifier.verifyAndExtractUserId(refreshTokenValue);

        RefreshToken refreshToken = refreshTokenLoader.loadValidToken(
                refreshTokenValue,
                LocalDateTime.now()
        );

        if (!refreshToken.isOwnedBy(userId)) {
            throw InvalidRefreshTokenException.ownerMismatch();
        }

        User user = userLoader.loadActiveUserById(userId);

        String newAccessToken = jwtTokenGenerator.generateAccessToken(
                user.getId(),
                user.getRole().name()
        );

        return AuthResponse.from(
                user,
                newAccessToken
        );
    }

}