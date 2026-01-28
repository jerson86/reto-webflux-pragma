package com.pragma.powerup.domain.model;

import com.pragma.powerup.domain.enums.Role;
import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
}
