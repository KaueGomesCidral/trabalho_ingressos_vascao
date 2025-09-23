package com.example.authservice.infrastructure.mail;

import com.example.authservice.application.ports.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class SmtpMailSender implements MailSender {
    private final JavaMailSender javaMailSender;

    @Autowired
    public SmtpMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMagicLink(String to, String magicUrl, java.time.Instant expiresAt) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Seu link mágico de acesso");
        message.setText("Clique no link para acessar: " + magicUrl + "\nO link expira em: " + expiresAt.toString());
        javaMailSender.send(message);
    }
}
