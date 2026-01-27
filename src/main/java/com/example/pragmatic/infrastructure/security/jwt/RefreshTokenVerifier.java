package com.example.pragmatic.infrastructure.security.jwt;

import com.example.pragmatic.domain.auth.InvalidRefreshTokenException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenVerifier {

    private final JwtProvider jwtProvider;

    public Long verifyAndExtractUserId(String refreshToken) {
        Claims claims = jwtProvider.parseAndValidate(refreshToken);

        if (!jwtProvider.isRefreshToken(claims)) {
            throw new InvalidRefreshTokenException("리프레시 토큰이 아닙니다.");
        }

        return jwtProvider.extractUserId(claims);
    }

}