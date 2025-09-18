package com.example.authservice.domain.auth.vo;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class ExpiresAt {
    
    @Column(name = "expires_at", nullable = false)
    private Instant value;
    
    public ExpiresAt(Instant value) {
        if (value == null) {
            throw new IllegalArgumentException("expiresAt é obrigatório");

        }

        this.value = value;
    }

    public static ExpiresAt of(Instant value) {
        return new ExpiresAt(value);
    }

    public boolean isAfter(Instant value) {
        return this.value.isAfter(value);
    }
}
