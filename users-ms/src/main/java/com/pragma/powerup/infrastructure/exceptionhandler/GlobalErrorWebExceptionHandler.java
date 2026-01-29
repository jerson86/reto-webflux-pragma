package com.pragma.powerup.infrastructure.exceptionhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.infrastructure.exception.TokenExpiredException;
import com.pragma.powerup.infrastructure.exception.TokenParsingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@Order(-2)
@RequiredArgsConstructor
@Slf4j
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof BusinessException || ex instanceof ValidationException) {
            log.warn("Business/Validation error: {}", ex.getMessage());
        } else {
            log.error("Technical error: {}", ex.getMessage(), ex);
        }

        HttpStatus status = determineHttpStatus(ex);
        String message = determineMessage(ex);
        String errorCode = determineErrorCode(ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                errorCode,
                exchange.getRequest().getPath().value()
        );

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(errorResponse);
        } catch (JsonProcessingException e) {
            log.error("Error serializing error response", e);
            bytes = "{}".getBytes();
        }

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private HttpStatus determineHttpStatus(Throwable ex) {
        if (ex instanceof UserAlreadyExistsException) {
            return HttpStatus.CONFLICT;
        } else if (ex instanceof UserNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof InvalidCredentialsException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof BusinessException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ValidationException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof TokenParsingException || ex instanceof TokenExpiredException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof DataIntegrityViolationException) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String determineMessage(Throwable ex) {
        if (ex instanceof BaseException || ex instanceof IllegalArgumentException) {
            return ex.getMessage();
        } else if (ex instanceof DataIntegrityViolationException) {
            return "El usuario ya se encuentra registrado.";
        }
        return "Error interno del servidor";
    }

    private String determineErrorCode(Throwable ex) {
        if (ex instanceof BaseException) {
            return ((BaseException) ex).getErrorCode();
        }
        return "INTERNAL_ERROR";
    }
}
