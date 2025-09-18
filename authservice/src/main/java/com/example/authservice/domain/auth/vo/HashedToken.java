package com.example.authservice.domain.auth.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class HashedToken {
    
    @Column(name = "hashedtoken")
    private String value;

    public HashedToken(String value) {
        String clearValue = value == null ? null : value.trim();

        if (clearValue == null || clearValue.isBlank()) {
            throw new IllegalArgumentException("Token inválido");
        }

        this.value = value;
    }

    public static HashedToken of (String value) {
        return new HashedToken(value);
    }
}
