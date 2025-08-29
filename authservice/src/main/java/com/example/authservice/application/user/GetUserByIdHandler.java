package com.example.authservice.application.user;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.interfaces.rest.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserByIdHandler {
    private final UserRepository userRepository;

    public UserResponse handle(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail().getValue(),
            user.getRole().getValue().name()
        );           
    };
}
