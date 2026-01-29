package com.pragma.powerup.infrastructure.input.rest.router;

import com.pragma.powerup.application.handler.IAuthHandler;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.infrastructure.input.rest.dto.LoginRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.RegisterRequest;
import com.pragma.powerup.infrastructure.input.rest.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthRouteHandler {

    private final IAuthHandler authHandler;

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(RegisterRequest.class)
                .doOnNext(req -> log.info("Register request: {}", req))
                .flatMap(this::validateRegisterRequest)
                .flatMap(authHandler::register)
                .flatMap(userResponse -> ServerResponse.status(HttpStatus.CREATED).bodyValue(userResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(this::validateLoginRequest)
                .flatMap(authHandler::login)
                .flatMap(authResponse -> ServerResponse.ok().bodyValue(authResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> validate(ServerRequest request) {
        return extractToken(request)
                .flatMap(authHandler::validateToken)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .doOnSuccess(response -> log.info("Token validation completed"))
                .onErrorResume(this::handleError);
    }

    private Mono<RegisterRequest> validateRegisterRequest(RegisterRequest request) {
        return Mono.just(request)
                .flatMap(req -> RequestValidator.validateNotBlank(req.name(), "Nombre")
                        .thenReturn(req))
                .flatMap(req -> RequestValidator.validateNotBlank(req.email(), "Email")
                        .thenReturn(req))
                .flatMap(req -> RequestValidator.validateNotBlank(req.password(), "Contraseña")
                        .thenReturn(req));
    }

    private Mono<LoginRequest> validateLoginRequest(LoginRequest request) {
        return Mono.just(request)
                .flatMap(req -> RequestValidator.validateNotBlank(req.email(), "Email")
                        .thenReturn(req))
                .flatMap(req -> RequestValidator.validateNotBlank(req.password(), "Contraseña")
                        .thenReturn(req));
    }

    private Mono<String> extractToken(ServerRequest request) {
        String header = request.headers().header(HttpHeaders.AUTHORIZATION).stream().findFirst().orElse("");
        log.info("Validate token: {}", header);
        
        if (header.isBlank() || !header.startsWith("Bearer ")) {
            return Mono.error(new DomainException("Header de autorización inválido"));
        }
        
        return Mono.just(header);
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        log.error("Error in AuthRouteHandler: {}", error.getMessage());
        return Mono.error(error);
    }
}
