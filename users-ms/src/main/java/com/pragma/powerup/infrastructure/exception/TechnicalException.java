package com.pragma.powerup.infrastructure.exception;

import com.pragma.powerup.domain.exception.BaseException;

public class TechnicalException extends BaseException {
    public TechnicalException(String message, String errorCode) {
        super(message, errorCode);
    }
}
