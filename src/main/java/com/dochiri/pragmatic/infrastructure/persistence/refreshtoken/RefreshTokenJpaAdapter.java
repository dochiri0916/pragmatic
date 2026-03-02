package com.dochiri.pragmatic.infrastructure.persistence.refreshtoken;

import com.dochiri.pragmatic.domain.auth.RefreshToken;
import com.dochiri.pragmatic.domain.auth.RefreshTokenRepository;
import com.dochiri.pragmatic.domain.common.exception.BaseException;
import com.dochiri.pragmatic.domain.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenJpaAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public void save(RefreshToken refreshToken) {
        refreshTokenJpaRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenJpaRepository.findByToken(token)
                .orElseThrow(() -> new BaseException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenJpaRepository.findByUserId(userId);
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenJpaRepository.deleteByToken(token);
    }

    @Override
    public long deleteByExpiresAtBefore(LocalDateTime now) {
        return refreshTokenJpaRepository.deleteByExpiresAtBefore(now);
    }

}