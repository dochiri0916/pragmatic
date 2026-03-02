package com.dochiri.pragmatic.application.auth.command;

import com.dochiri.pragmatic.domain.auth.RefreshToken;
import com.dochiri.pragmatic.domain.auth.RefreshTokenRepository;
import com.dochiri.pragmatic.infrastructure.security.jwt.RefreshTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VerifyRefreshTokenService {

    private final RefreshTokenVerifier refreshTokenVerifier;
    private final RefreshTokenRepository refreshTokenRepository;

    public Output execute(Input input) {
        Long userId = refreshTokenVerifier.verifyAndExtractUserId(input.token());

        RefreshToken refreshToken = refreshTokenRepository.findByToken(input.token());
        refreshToken.verifyNotExpired(LocalDateTime.now());
        refreshToken.verifyOwnership(userId);

        return new Output(userId, refreshToken);
    }

    public record Input(
            String token
    ) {
        public Input {
            Objects.requireNonNull(token);
        }
    }

    public record Output(
            Long userId,
            RefreshToken refreshToken
    ) {
    }

}
