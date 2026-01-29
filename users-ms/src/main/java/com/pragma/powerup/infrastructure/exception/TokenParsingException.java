package com.pragma.powerup.infrastructure.exception;

public class TokenParsingException extends TokenException {
    public TokenParsingException(String message) {
        super(message, "TOKEN_001");
    }
}
