package com.pragma.powerup.infrastructure.input.rest.router;

import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.utils.Constants;
import com.pragma.powerup.domain.enums.Role;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.infrastructure.input.rest.dto.AuthResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.LoginRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.RegisterRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.UserResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.UserValidationResponse;
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

    private final IAuthServicePort authServicePort;

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(RegisterRequest.class)
                .doOnNext(req -> log.info("Register request: {}", req))
                .flatMap(this::validateRegisterRequest)
                .map(this::mapToUser)
                .flatMap(authServicePort::register)
                .map(this::mapToUserResponse)
                .flatMap(userResponse -> ServerResponse.status(HttpStatus.CREATED).bodyValue(userResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(this::validateLoginRequest)
                .flatMap(req -> authServicePort.login(req.email(), req.password()))
                .map(AuthResponse::new)
                .flatMap(authResponse -> ServerResponse.ok().bodyValue(authResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> validate(ServerRequest request) {
        return extractToken(request)
                .flatMap(authServicePort::validateToken)
                .map(validation -> new UserValidationResponse(validation.getUserId(), validation.getRole()))
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .doOnSuccess(response -> log.info("Token validation completed"))
                .onErrorResume(this::handleError);
    }

    // DTO Mapping methods
    private User mapToUser(RegisterRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .role(Role.valueOf(request.role().toUpperCase()))
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    // Validation methods
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
