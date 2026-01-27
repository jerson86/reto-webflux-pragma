package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.handler.IAuthHandler;
import com.pragma.powerup.application.mapper.IUserRequestMapper;
import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.infrastructure.input.rest.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthHandler implements IAuthHandler {

    private final IAuthServicePort authServicePort;
    private final IUserRequestMapper userRequestMapper;

    @Override
    public Mono<UserResponse> register(RegisterRequest request) {
        User user = userRequestMapper.toDomain(request);
        return authServicePort.register(user)
                .map(userRequestMapper::toResponse);
    }

    @Override
    public Mono<AuthResponse> login(LoginRequest request) {
        return authServicePort.login(request.email(), request.password())
                .map(AuthResponse::new);
    }

    @Override
    public Mono<UserValidationResponse> validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error(new DomainException("Header de autorización inválido"));
        }
        String token = authHeader.substring(7);

        return authServicePort.validateToken(token)
                .map(userRequestMapper::toResponse);
    }
}
