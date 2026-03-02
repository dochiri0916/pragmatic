package com.dochiri.pragmatic.infrastructure.security.cookie;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieProvider {

    private final CookieProperties cookieProperties;

    public void addRefreshToken(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = buildCookie(
                cookieProperties.refreshTokenName(),
                refreshToken,
                cookieProperties.maxAge()
        );
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteRefreshToken(HttpServletResponse response) {
        ResponseCookie cookie = buildCookie(
                cookieProperties.refreshTokenName(),
                "",
                0
        );
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private ResponseCookie buildCookie(String name, String value, long maxAge) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .path(cookieProperties.path())
                .maxAge(maxAge)
                .httpOnly(cookieProperties.httpOnly())
                .secure(cookieProperties.secure());

        if (cookieProperties.domain() != null && !cookieProperties.domain().isBlank()) {
            builder.domain(cookieProperties.domain());
        }

        if (cookieProperties.sameSite() != null && !cookieProperties.sameSite().isBlank()) {
            builder.sameSite(cookieProperties.sameSite());
        }

        return builder.build();
    }

}