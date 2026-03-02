package com.dochiri.pragmatic.infrastructure.security.cookie;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cookie")
public record CookieProperties(
        String refreshTokenName,
        String domain,
        String path,
        boolean httpOnly,
        boolean secure,
        String sameSite,
        long maxAge
) {
}
