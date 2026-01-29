package com.pragma.powerup.domain.exception;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(message, "USER_002");
    }
}
