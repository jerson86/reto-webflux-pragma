package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UserValidation {
    Long userId;
    String role;
}
