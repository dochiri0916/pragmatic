package com.dochiri.pragmatic.infrastructure.security.jwt;

import com.dochiri.pragmatic.domain.common.exception.BaseException;
import com.dochiri.pragmatic.domain.common.exception.ErrorCode;
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
            throw new BaseException(ErrorCode.INVALID_REFRESH_TOKEN_TYPE);
        }

        return jwtProvider.extractUserId(claims);
    }

}