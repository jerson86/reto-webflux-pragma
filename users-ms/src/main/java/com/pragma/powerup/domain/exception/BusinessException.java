package com.pragma.powerup.domain.exception;

public class BusinessException extends BaseException {
    public BusinessException(String message, String errorCode) {
        super(message, errorCode);
    }
}
