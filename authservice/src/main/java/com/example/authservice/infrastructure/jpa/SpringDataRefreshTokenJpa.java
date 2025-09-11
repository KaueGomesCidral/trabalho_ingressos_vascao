package com.example.authservice.infrastructure.jpa;

import com.example.authservice.domain.refresh.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataRefreshTokenJpa extends JpaRepository<RefreshToken, UUID> {
    @Query("SELECT r FROM RefreshToken r WHERE r.tokenHash.value = :hash AND r.revoked = false AND r.expiresAt.value > :now")
    Optional<RefreshToken> findActiveByHash(String hash, Instant now);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.id = :id")
    void revoke(UUID id);
}
