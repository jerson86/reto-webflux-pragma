package com.pragma.powerup.application.handler;

import com.pragma.powerup.infrastructure.input.rest.dto.*;
import reactor.core.publisher.Mono;

public interface IAuthHandler {
    Mono<UserResponse> register(RegisterRequest request);
    Mono<AuthResponse> login(LoginRequest request);
    Mono<UserValidationResponse> validateToken(String token);
}
