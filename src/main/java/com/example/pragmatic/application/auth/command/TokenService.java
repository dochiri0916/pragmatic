package com.example.pragmatic.application.auth.command;

import com.example.pragmatic.domain.auth.RefreshToken;
import com.example.pragmatic.domain.auth.RefreshTokenExpiredException;
import com.example.pragmatic.domain.auth.RefreshTokenNotFoundException;
import com.example.pragmatic.infrastructure.persistence.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void issueRefreshToken(
            final String token,
            final Long userId,
            final LocalDateTime expiresAt
    ) {
        refreshTokenRepository.save(
                RefreshToken.issue(token, userId, expiresAt)
        );
    }

    @Transactional
    public void revokeByToken(final String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Transactional
    public RefreshToken findValidToken(
            final String token,
            final LocalDateTime now
    ) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException("유효하지 않은 토큰입니다."));

        if (refreshToken.isExpired(now)) {
            throw new RefreshTokenExpiredException("만료된 토큰입니다.");
        }

        return refreshToken;
    }

}