package com.dochiri.pragmatic.infrastructure.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "swagger")
public record SwaggerProperties(
        Auth auth,
        List<String> securityMatchers
) {
    public record Auth(
            String username,
            String password
    ) {
    }
}
