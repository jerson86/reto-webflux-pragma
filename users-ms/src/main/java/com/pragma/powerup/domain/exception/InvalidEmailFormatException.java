package com.pragma.powerup.domain.exception;

public class InvalidEmailFormatException extends ValidationException {
    public InvalidEmailFormatException(String message) {
        super(message, "VAL_002");
    }
}
