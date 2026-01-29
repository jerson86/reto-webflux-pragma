package com.pragma.powerup.domain.exception;

public class EmptyFieldException extends ValidationException {
    public EmptyFieldException(String message) {
        super(message, "VAL_001");
    }
}
