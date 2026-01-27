package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.handler.IAuthHandler;
import com.pragma.powerup.infrastructure.input.rest.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final IAuthHandler authHandler;

    @PostMapping("/register")
    public Mono<ResponseEntity<UserResponse>> register(@RequestBody RegisterRequest request) {
        log.info("Register request: {}", request);
        return authHandler.register(request)
                .map(response -> new ResponseEntity<>(response, HttpStatus.CREATED));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody LoginRequest request) {
        return authHandler.login(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/validate")
    public Mono<ResponseEntity<UserValidationResponse>> validate(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        log.info("Validate token: {}", token);
        return authHandler.validateToken(token)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("Token validado: {}", response));
    }
}