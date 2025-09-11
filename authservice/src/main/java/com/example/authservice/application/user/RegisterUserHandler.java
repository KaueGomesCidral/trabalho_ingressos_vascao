package com.example.authservice.application.user;

import com.example.authservice.application.ports.PasswordHasher;
import com.example.authservice.domain.user.User;
import com.example.authservice.domain.user.UserRepository;
import com.example.authservice.domain.user.vo.Email;
import com.example.authservice.domain.user.vo.RoleType;
import com.example.authservice.interfaces.rest.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RegisterUserHandler {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserResponse handle(String name, String emailRaw, String password) {
        Email email = Email.of(emailRaw);

        String hash = passwordHasher.hash(password);
        User user = new User(name, hash, email, RoleType.CUSTOMER);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail().getValue(),
                savedUser.getRole().getValue().name()
        );
    }
}
