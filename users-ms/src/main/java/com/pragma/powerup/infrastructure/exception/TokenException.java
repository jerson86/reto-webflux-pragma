package com.pragma.powerup.infrastructure.exception;

public class TokenException extends TechnicalException {
    public TokenException(String message, String errorCode) {
        super(message, errorCode);
    }
}
