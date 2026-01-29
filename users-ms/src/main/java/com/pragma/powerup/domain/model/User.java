package com.pragma.powerup.domain.model;

import com.pragma.powerup.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@AllArgsConstructor
@Builder
public class User {
    @With Long id;
    String name;
    String email;
    String password;
    Role role;

    public User withEncryptedPassword(String encryptedPassword) {
        return new User(this.id, this.name, this.email, encryptedPassword, this.role);
    }

    public UserDetail toUserDetail() {
        return new UserDetail(this.name, this.email);
    }
}
