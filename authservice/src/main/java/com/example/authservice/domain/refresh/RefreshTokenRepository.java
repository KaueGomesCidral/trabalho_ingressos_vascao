package com.example.authservice.domain.refresh;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findActiveByHash(String tokenHashHex, Instant now);
    void revoke(UUID id);
    void deleteById(UUID id);
}
