package com.dochiri.pragmatic.application.auth.facade;

import com.dochiri.pragmatic.application.auth.dto.LoginResult;
import com.dochiri.pragmatic.domain.auth.RefreshToken;
import com.dochiri.pragmatic.domain.auth.RefreshTokenRepository;
import com.dochiri.pragmatic.domain.user.User;
import com.dochiri.pragmatic.domain.user.UserRepository;
import com.dochiri.pragmatic.infrastructure.security.jwt.JwtTokenGenerator;
import com.dochiri.pragmatic.infrastructure.security.jwt.JwtTokenResult;
import com.dochiri.pragmatic.infrastructure.security.jwt.RefreshTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReissueTokenFacade {

    private final RefreshTokenVerifier refreshTokenVerifier;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;

    @Transactional
    public LoginResult reissue(String refreshTokenValue) {
        Long userId = refreshTokenVerifier.verifyAndExtractUserId(refreshTokenValue);

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue);

        refreshToken.verifyNotExpired(LocalDateTime.now());
        refreshToken.verifyOwnership(userId);

        User user = userRepository.findByIdAndDeletedAtIsNull(userId);

        JwtTokenResult tokenResult = jwtTokenGenerator.generateToken(user.getId(), user.getRole().name());

        refreshToken.rotate(tokenResult.refreshToken(), tokenResult.refreshTokenExpiresAt());
        refreshTokenRepository.save(refreshToken);

        return LoginResult.from(user, tokenResult.accessToken(), tokenResult.refreshToken());
    }

}
