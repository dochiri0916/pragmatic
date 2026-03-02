package com.dochiri.pragmatic.domain.auth;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);

    RefreshToken findByToken(String token);

    Optional<RefreshToken> findByUserId(Long userId);

    void deleteByToken(String token);

    long deleteByExpiresAtBefore(LocalDateTime now);

}