package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UserDetail {
    String name;
    String email;
}
