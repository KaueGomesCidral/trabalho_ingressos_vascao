package com.example.authservice.application.ports;

import java.time.Instant;

public interface MailSender {
    void sendMagicLink(
        String to,
        String magicUrl,
        Instant expiresAt
    );
    
}
