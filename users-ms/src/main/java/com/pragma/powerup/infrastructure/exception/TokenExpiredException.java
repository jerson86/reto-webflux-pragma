package com.pragma.powerup.infrastructure.exception;

public class TokenExpiredException extends TokenException {
    public TokenExpiredException(String message) {
        super(message, "TOKEN_002");
    }
}
