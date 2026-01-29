package com.pragma.powerup.domain.exception;

public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException(String message) {
        super(message, "USER_001");
    }
}
