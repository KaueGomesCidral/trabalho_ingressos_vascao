package com.example.authservice.application.auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.authservice.application.ports.PasswordHasher;
import com.example.authservice.application.ports.TokenService;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.domain.user.vo.Email;
import com.example.authservice.interfaces.rest.dto.auth.TokenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordLoginHandler {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public TokenResponse handle(String emailRaw, String pwRaw) {
        Email email = Email.of(emailRaw);
        Optional<User> userOpt = userRepository.findByEmail(email.getValue());

        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credencial Invalida");
        }

        User user = userOpt.get();
        if (passwordHasher.matches(pwRaw, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credencial Invalida");
        }

        TokenService.TokenPair pair = tokenService.issue(user);

        return new TokenResponse(
            pair.accessToken(),
            pair.refreshToken(),
            pair.expiresInSeconds()
        );
    }
}

