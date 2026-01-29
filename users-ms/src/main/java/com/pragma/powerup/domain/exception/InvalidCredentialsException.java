package com.pragma.powerup.domain.exception;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException(String message) {
        super(message, "AUTH_001");
    }
}
