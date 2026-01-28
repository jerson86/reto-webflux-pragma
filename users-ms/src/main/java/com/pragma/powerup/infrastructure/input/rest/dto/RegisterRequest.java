package com.pragma.powerup.infrastructure.input.rest.dto;

public record RegisterRequest(String name, String email, String password, String role) {}
