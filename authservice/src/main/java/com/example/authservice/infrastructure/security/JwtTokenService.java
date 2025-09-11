package com.example.authservice.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.authservice.application.ports.TokenService;
import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.RefreshTokenRepository;
import com.example.authservice.domain.refresh.vo.ExpiresAt;
import com.example.authservice.domain.refresh.vo.TokenHash;
import com.example.authservice.domain.user.User;
import com.example.authservice.infrastructure.config.JwtProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {
    private final JwtProperties props;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();


    @Override
    public TokenPair issue(User user) {
        if (props.getSecret() == null || props.getSecret().isBlank()) {
            throw new IllegalStateException("Secret is Mandatory (jwt.secret)");

        }

        Instant now = Instant.now();
        Algorithm algorithm = Algorithm.HMAC256(props.getSecret().getBytes(StandardCharsets.UTF_8));

        Instant accessExpires = now.plusSeconds(props.getAccessTtlSeconds());
        String accessToken = JWT.create()
            .withIssuer(props.getIssuer())
            .withAudience(props.getAudience())
            .withSubject(user.getId().toString())
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(accessExpires))
            .withClaim("type", "access")
            .withClaim("email", user.getEmail().getValue())
            .withClaim("role", user.getRole().getValue().name())
            .withClaim("level", user.getRole().getValue().getLevel())
            .sign(algorithm);

            
            // --- gera refresh token "raw" (cliente) ---
            byte[] rnd = new byte[64];
            secureRandom.nextBytes(rnd);
            String refreshRaw = Base64.getUrlEncoder().withoutPadding().encodeToString(rnd);

            // calcula hash para armazenar
            String tokenHashHex = RefreshTokenHasher.sha256Hex(refreshRaw);

            // cria e persiste RefreshToken (somente hash é salvo)
            Instant refreshExpires = now.plusSeconds(props.getRefreshTtlSeconds());
            RefreshToken rt = new RefreshToken(user, new TokenHash(tokenHashHex), new ExpiresAt(refreshExpires));
            refreshTokenRepository.save(rt);
            
            return new TokenPair(accessToken, "", (int) props.getAccessTtlSeconds());
    }
}



  


