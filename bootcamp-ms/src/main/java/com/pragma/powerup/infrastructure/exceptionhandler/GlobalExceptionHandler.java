package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.ErrorDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Error de integridad: El recurso no puede ser procesado porque tiene dependencias activas.";
        // Si el error es un duplicado de nombre (HU1/HU3/HU4)
        if (ex.getMessage() != null && ex.getMessage().contains("unique")) {
            message = "Error: El nombre ya existe en el sistema.";
        }
        return buildResponse(HttpStatus.CONFLICT, message);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorDetails> handleWebClientException(WebClientResponseException ex) {
        String message = "Error al comunicarse con el servicio externo: " + ex.getStatusCode();
        return buildResponse(HttpStatus.BAD_GATEWAY, message);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorDetails> handleDomainException(DomainException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErrorDetails> buildResponse(HttpStatus status, String message) {
        ErrorDetails details = new ErrorDetails(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return new ResponseEntity<>(details, status);
    }
}