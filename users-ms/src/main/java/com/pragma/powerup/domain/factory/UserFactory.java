package com.pragma.powerup.domain.factory;

import com.pragma.powerup.domain.enums.Role;
import com.pragma.powerup.domain.exception.EmptyFieldException;
import com.pragma.powerup.domain.exception.InvalidEmailFormatException;
import com.pragma.powerup.domain.exception.InvalidPasswordException;
import com.pragma.powerup.domain.model.User;

public class UserFactory {

    public static User createUser(String name, String email, String password, Role role) {
        validateUserData(name, email, password, role);
        
        return User.builder()
                .name(name.trim())
                .email(email.trim().toLowerCase())
                .password(password)
                .role(role)
                .build();
    }

    public static User createUserWithId(Long id, String name, String email, String password, Role role) {
        User user = createUser(name, email, password, role);
        return user.withId(id);
    }

    private static void validateUserData(String name, String email, String password, Role role) {
        if (name == null || name.isBlank()) {
            throw new EmptyFieldException("El nombre es requerido");
        }
        if (email == null || email.isBlank()) {
            throw new EmptyFieldException("El email es requerido");
        }
        if (!isValidEmail(email)) {
            throw new InvalidEmailFormatException("El formato del email es inválido");
        }
        if (password == null || password.isBlank()) {
            throw new EmptyFieldException("La contraseña es requerida");
        }
        if (password.length() < 6) {
            throw new InvalidPasswordException("La contraseña debe tener al menos 6 caracteres");
        }
        if (role == null) {
            throw new EmptyFieldException("El rol es requerido");
        }
    }

    private static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
