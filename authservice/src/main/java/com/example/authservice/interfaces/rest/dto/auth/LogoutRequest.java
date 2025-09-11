package com.example.authservice.interfaces.rest.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(@NotBlank String refreshToken) {}
