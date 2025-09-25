package com.example.authservice.application.auth;

import java.time.Instant;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.authservice.application.ports.MailSender;
import com.example.authservice.domain.auth.MagicLink;
import com.example.authservice.domain.auth.MagicLinkRepository;
import com.example.authservice.domain.auth.vo.ExpiresAt;
import com.example.authservice.domain.auth.vo.HashedToken;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.domain.user.vo.Email;
import com.example.authservice.infrastructure.config.AppProperties;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class RequestMagicLinkHandler {
    private final UserRepository userRepository;
    private final MagicLinkRepository magicLinkRepository;
    private final MailSender mailSender;
    private final AppProperties appProperties;

    public record Result(boolean accepted) {};

    public Result handle(String emailRaw) {
        Email email = Email.of(emailRaw);

        Optional<User> userOpt = userRepository.findByEmail(email.getValue());
        if (userOpt.isEmpty()) {
            return new Result(true);
        }

        User user = userOpt.get();
        String token = "";
        String hash = "";

        Instant now = Instant.now();
        long ttl = appProperties.getMagicLink().getTtlSeconds();
        Instant expiresAt = now.plusSeconds(ttl);

        MagicLink link = MagicLink.issueForLogin(
            user.getId(),
            HashedToken.of(hash),
            ExpiresAt.of(expiresAt)
        );

        magicLinkRepository.save(link);

        String base = appProperties.getMagicLink().getVerifyUrlBase();
        String url = base + (base.contains("?") ? "&" : "?") +
                "token" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        mailSender.sendMagicLink(
            email.getValue(),
            url,
            expiresAt
        );
        return new Result(true);

    }
}
