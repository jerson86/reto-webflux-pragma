package com.pragma.powerup.domain.exception;

public class ValidationException extends BaseException {
    public ValidationException(String message, String errorCode) {
        super(message, errorCode);
    }
}
