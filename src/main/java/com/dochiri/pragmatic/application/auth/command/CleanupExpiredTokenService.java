package com.dochiri.pragmatic.application.auth.command;

import com.dochiri.pragmatic.domain.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CleanupExpiredTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public long execute(Input input) {
        return refreshTokenRepository.deleteByExpiresAtBefore(input.now());
    }

    public record Input(
            LocalDateTime now
    ) {
        public Input {
            Objects.requireNonNull(now);
        }
    }

}