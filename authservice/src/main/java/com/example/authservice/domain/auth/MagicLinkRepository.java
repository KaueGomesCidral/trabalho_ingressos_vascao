package com.example.authservice.domain.auth;

import java.time.Instant;
import java.util.Optional;

public interface MagicLinkRepository {
    MagicLink save(MagicLink magicLink);
    Optional<MagicLink> findValidByHash(String hash, Instant now);
    
}
