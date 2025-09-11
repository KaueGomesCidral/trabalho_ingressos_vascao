package com.example.authservice.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.application.auth.PasswordLoginHandler;
import com.example.authservice.application.auth.RefreshTokenHandler;
import com.example.authservice.interfaces.rest.dto.auth.PasswordLoginRequest;
import com.example.authservice.interfaces.rest.dto.auth.RefreshRequest;
import com.example.authservice.interfaces.rest.dto.auth.LogoutRequest;
import com.example.authservice.interfaces.rest.dto.auth.TokenResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordLoginHandler passwordLoginHandler;
    private final RefreshTokenHandler refreshTokenHandler;

    @PostMapping("/login/password")
    public ResponseEntity<TokenResponse> loginPassword(@Valid @RequestBody PasswordLoginRequest request) {
        var token = passwordLoginHandler.handle(request.email(), request.password());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        var pair = refreshTokenHandler.refresh(request.refreshToken());
        return ResponseEntity.ok(new TokenResponse(
            pair.accessToken(),          
            pair.refreshToken(),
            pair.expiresInSeconds()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        refreshTokenHandler.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
