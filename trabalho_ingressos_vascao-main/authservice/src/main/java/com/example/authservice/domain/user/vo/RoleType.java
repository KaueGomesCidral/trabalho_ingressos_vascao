package com.example.authservice.domain.user.vo;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;

import lombok.Getter;

@Getter
public enum RoleType {
    CUSTOMER(1),
    ADMIN(2);

    private final int level;

    RoleType(int level) {
        this.level = level;
    }

    public boolean covers(RoleType other) {
        return this.level >= other.level;
    }
}
