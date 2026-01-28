package com.pragma.powerup.infrastructure.out.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Data
public class UserEntity {
    @Id
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
}
