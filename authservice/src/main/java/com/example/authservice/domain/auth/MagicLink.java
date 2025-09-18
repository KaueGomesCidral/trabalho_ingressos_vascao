package com.example.authservice.domain.auth;

import java.time.Instant;
import java.util.UUID;

import com.example.authservice.domain.auth.vo.ExpiresAt;
import com.example.authservice.domain.auth.vo.HashedToken;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "magic_link")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class MagicLink {
    
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column
    private Instant consumedAt;

    @Embedded
    private HashedToken hashedtoken;
    
    @Embedded
    private ExpiresAt expiresAt;

    public MagicLink(UUID userId, HashedToken hashedToken, ExpiresAt expiresAt) {
        if (userId == null) {
            throw new IllegalArgumentException("userId é obrigatório");
        }

        if (hashedToken == null) {
            throw new IllegalArgumentException("hashedToken é obrigatório");
        }

        if (expiresAt == null) {
            throw new IllegalArgumentException("expiresAt é obrigatório");
    }

    this.userId = userId;
    this.hashedtoken = hashedToken;
    this.expiresAt = expiresAt;
}

public static MagicLink issueForLogin(UUID userId, HashedToken hashedToken, ExpiresAt expiresAt) {
    return new MagicLink(userId, hashedToken, expiresAt);
}

public boolean isExpired(Instant now) {
    return !this.expiresAt.isAfter(now);
}

public boolean isConsumed() {
    return this.consumedAt != null;
}

public void consume(Instant now) {
    if (this.isConsumed()) {
        throw new IllegalStateException("Token já consumido");
    }

    if (this.isExpired(now)) {
        throw new IllegalStateException("Token expirado");

    }

    this.consumedAt = now;
}

}

