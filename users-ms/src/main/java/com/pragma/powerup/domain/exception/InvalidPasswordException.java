package com.pragma.powerup.domain.exception;

public class InvalidPasswordException extends ValidationException {
    public InvalidPasswordException(String message) {
        super(message, "VAL_003");
    }
}
