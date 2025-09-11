package com.example.authservice.domain.refresh.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class TokenHash {
    @Getter
    @Column(name = "token_hash", nullable = false, length = 64)
    private String value;

    public TokenHash(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("tokenHash inválido");
        }
        if (value.length() != 64) {
            throw new IllegalArgumentException("tokenHash deve ser SHA-256 hex (64 chars)");
        }
        this.value = value;
    }
}
