package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetail {
    private String name;
    private String email;
}
