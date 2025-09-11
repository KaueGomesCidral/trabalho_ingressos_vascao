package com.example.authservice.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
@Getter
public class JwtProperties {
    private String secret;
    private String issuer = "auth-service";
    private String audience = "deliveryapp";
    private long accessTtlSeconds = 900;
    private long refreshTtlSeconds = 2_592_000;
}

