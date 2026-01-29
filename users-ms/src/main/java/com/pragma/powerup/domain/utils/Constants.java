package com.pragma.powerup.domain.utils;

public class Constants {
    // Security
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String E_ROLE = "role";
    public static final String E_ID = "id";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String BEARER_KEY = "bearer-key";
    public static final String AUTH_TAG = "Auth";
    public static final String USER_TAG = "User";

    // Resilience
    public static final String DB_CIRCUIT_BREAKER = "database";

    // Validation Messages
    public static final String EMPTY_FIELD = " no puede estar vacío";
    public static final String EMPTY_FIELD_FEMALE = " no puede estar vacía";
    public static final String INVALID_FORMAT = "El formato es inválido";
    public static final String INVALID_EMAIL = "El formato del email es inválido";
    public static final String SHORT_PASSWORD = "La contraseña debe tener al menos 6 caracteres";
    
    // Error Codes
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
    
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}
