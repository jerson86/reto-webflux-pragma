package com.pragma.powerup.domain.model;

import lombok.Data;

@Data
public class AuthenticatedUser {
    private Long userId;
    private String role;
}
