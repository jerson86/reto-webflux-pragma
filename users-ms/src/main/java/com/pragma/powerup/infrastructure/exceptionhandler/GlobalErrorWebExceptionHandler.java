package com.pragma.powerup.infrastructure.exceptionhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.domain.exception.DomainException;
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
        log.error("Error handling request: {}", ex.getMessage(), ex);

        HttpStatus status = determineHttpStatus(ex);
        String message = determineMessage(ex);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
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
        if (ex instanceof DomainException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof DataIntegrityViolationException) {
            return HttpStatus.CONFLICT;
        } else if (ex instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String determineMessage(Throwable ex) {
        if (ex instanceof DomainException) {
            return ex.getMessage();
        } else if (ex instanceof DataIntegrityViolationException) {
            return "El usuario ya se encuentra registrado.";
        } else if (ex instanceof IllegalArgumentException) {
            return ex.getMessage();
        }
        return "Error interno del servidor";
    }
}
