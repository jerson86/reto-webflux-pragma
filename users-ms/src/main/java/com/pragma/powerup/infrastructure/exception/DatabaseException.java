package com.pragma.powerup.infrastructure.exception;

public class DatabaseException extends TechnicalException {
    public DatabaseException(String message) {
        super(message, "DB_001");
    }
}
