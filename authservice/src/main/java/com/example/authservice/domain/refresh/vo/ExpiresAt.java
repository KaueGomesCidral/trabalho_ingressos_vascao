package com.example.authservice.domain.refresh.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Embeddable
@NoArgsConstructor
public class ExpiresAt {
    @Getter
    @Column(name = "expires_at", nullable = false)
    private Instant value;

    public ExpiresAt(Instant value) {
        if (value == null) {
            throw new IllegalArgumentException("expiresAt inválido");
        }
        this.value = value;
    }
}
