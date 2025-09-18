package com.example.authservice.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {
    private final MagicLink magicLink = new MagicLink();


    @Getter
    @Setter
    public static class MagicLink {
        private long ttlSeconds = 900;
        private String verifyUrlBase;
    }
}
