package com.example.authservice.infrastructure.persistence;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import com.example.authservice.domain.auth.MagicLink;
import com.example.authservice.domain.auth.MagicLinkRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaMagicLinkRepository implements MagicLinkRepository {
    private final SpringDataMagicLinkJpa jpa;

    @Override
    public MagicLink save(MagicLink magicLink) {
        return jpa.save(magicLink);
    }

    @Override
    public Optional<MagicLink> findValidByHash(String hash, Instant now){
        return jpa.findByHashedTokenValueAndConsumedAtIsNullAndExpiresAtValueIsGreaterThan(hash, now);
    }
}
