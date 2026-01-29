package com.pragma.powerup.infrastructure.input.rest.router;

import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.utils.Constants;
import com.pragma.powerup.infrastructure.input.rest.dto.LoginRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.RegisterRequest;
import com.pragma.powerup.infrastructure.input.rest.mapper.IAuthRestMapper;
import com.pragma.powerup.infrastructure.input.rest.validator.AuthHandlerValidator;
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

    private final IAuthServicePort authServicePort;
    private final IAuthRestMapper authRestMapper;
    private final AuthHandlerValidator authHandlerValidator;

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(RegisterRequest.class)
                .doOnNext(req -> log.info("Register request: {}", req))
                .flatMap(authHandlerValidator::validateRegisterRequest)
                .map(authRestMapper::toUser)
                .flatMap(authServicePort::register)
                .map(authRestMapper::toUserResponse)
                .flatMap(userResponse -> ServerResponse.status(HttpStatus.CREATED).bodyValue(userResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(authHandlerValidator::validateLoginRequest)
                .flatMap(req -> authServicePort.login(req.email(), req.password()))
                .map(authRestMapper::toAuthResponse)
                .flatMap(authResponse -> ServerResponse.ok().bodyValue(authResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> validate(ServerRequest request) {
        return extractToken(request)
                .flatMap(authServicePort::validateToken)
                .map(authRestMapper::toValidationResponse)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .doOnSuccess(response -> log.info("Token validation completed"))
                .onErrorResume(this::handleError);
    }

    private Mono<String> extractToken(ServerRequest request) {
        String header = request.headers().header(HttpHeaders.AUTHORIZATION).stream().findFirst().orElse("");
        log.info("Validate token: {}", header);
        
        if (header.isBlank() || !header.startsWith(Constants.BEARER_PREFIX)) {
            return Mono.error(new DomainException("Header de autorización inválido"));
        }
        
        String token = header.substring(7);
        return Mono.just(token);
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        log.error("Error in AuthRouteHandler: {}", error.getMessage());
        return Mono.error(error);
    }
}
