package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserValidation {
    private Long userId;
    private String role;
}
