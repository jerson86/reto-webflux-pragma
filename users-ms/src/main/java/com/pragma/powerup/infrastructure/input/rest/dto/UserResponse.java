package com.pragma.powerup.infrastructure.input.rest.dto;

public record UserResponse(
        Long id,
        String email,
        String role
) {}
