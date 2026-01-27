package com.pragma.powerup.infrastructure.input.rest.dto;

public record UserValidationResponse(
        Long userId,
        String role
) {}
