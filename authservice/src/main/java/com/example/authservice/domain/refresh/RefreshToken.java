package com.example.authservice.domain.refresh;

import com.example.authservice.domain.refresh.vo.ExpiresAt;
import com.example.authservice.domain.refresh.vo.TokenHash;
import com.example.authservice.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@NoArgsConstructor
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Embedded
    private TokenHash tokenHash;

    @Embedded
    private ExpiresAt expiresAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    public RefreshToken(User user, TokenHash tokenHash, ExpiresAt expiresAt) {
        this.user = user;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.revoked = false;
    }

    public boolean isActive(Instant now) {
        return !revoked && expiresAt.getValue().isAfter(now);
    }

    public void revoke() {
        this.revoked = true;
    }
}
