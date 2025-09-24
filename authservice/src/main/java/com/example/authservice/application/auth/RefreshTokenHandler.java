package com.example.authservice.application.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.authservice.application.ports.TokenService;
import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.RefreshTokenRepository;
import com.example.authservice.infrastructure.config.JwtProperties;
import com.example.authservice.infrastructure.security.RefreshTokenHasher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class RefreshTokenHandler {
    private final RefreshTokenRepository refreshRepo;
    private final TokenService tokenService;
    private final JwtProperties props;

    public RefreshTokenHandler(RefreshTokenRepository refreshRepo, TokenService tokenService, JwtProperties props) {
        this.refreshRepo = refreshRepo;
        this.tokenService = tokenService;
        this.props = props;
    }

    public TokenService.TokenPair refresh(String refreshTokenRaw) {
        if (refreshTokenRaw == null || refreshTokenRaw.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "refresh token é obrigatório");
        }

        
        try {
            Algorithm alg = Algorithm.HMAC256(props.getSecret().getBytes(StandardCharsets.UTF_8));
            JWT.require(alg)
                    .withIssuer(props.getIssuer())
                    .withAudience(props.getAudience())
                    .build()
                    .verify(refreshTokenRaw);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh inválido");
        }

        String hash = RefreshTokenHasher.sha256Hex(refreshTokenRaw);
        RefreshToken rt = refreshRepo.findActiveByHash(hash, Instant.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh inválido ou expirado"));

        
        TokenService.TokenPair pair = tokenService.issue(rt.getUser());

        
        refreshRepo.revoke(rt.getId());

        return pair;
    }

    public void logout(String refreshTokenRaw) {
        if (refreshTokenRaw == null || refreshTokenRaw.isBlank()) {
            return;
        }
        String hash = RefreshTokenHasher.sha256Hex(refreshTokenRaw);
        refreshRepo.findActiveByHash(hash, Instant.now())
                .ifPresent(rt -> refreshRepo.revoke(rt.getId()));
    }
}
