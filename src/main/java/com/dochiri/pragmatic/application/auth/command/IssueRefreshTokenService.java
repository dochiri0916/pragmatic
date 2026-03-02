package com.dochiri.pragmatic.application.auth.command;

import com.dochiri.pragmatic.domain.auth.RefreshToken;
import com.dochiri.pragmatic.domain.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IssueRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void execute(Input input) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(input.userId())
                .map(existing -> {
                    existing.rotate(input.token(), input.expiresAt());
                    return existing;
                })
                .orElseGet(() ->
                        RefreshToken.issue(
                                input.token(),
                                input.userId(),
                                input.expiresAt()
                        )
                );

        refreshTokenRepository.save(refreshToken);
    }

    public record Input(
            String token,
            Long userId,
            LocalDateTime expiresAt
    ) {
        public Input {
            Objects.requireNonNull(token);
            Objects.requireNonNull(userId);
            Objects.requireNonNull(expiresAt);
        }
    }

}