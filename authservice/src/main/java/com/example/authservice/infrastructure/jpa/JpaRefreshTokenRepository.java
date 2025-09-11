package com.example.authservice.infrastructure.jpa;

import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaRefreshTokenRepository implements RefreshTokenRepository {
    private final SpringDataRefreshTokenJpa jpa;

    @Override
    public RefreshToken save(RefreshToken token) {
        return jpa.save(token);
    }

    @Override
    public Optional<RefreshToken> findActiveByHash(String tokenHashHex, Instant now) {
        return jpa.findActiveByHash(tokenHashHex, now);
    }

    @Override
    @Transactional
    public void revoke(UUID id) {
        jpa.revoke(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
