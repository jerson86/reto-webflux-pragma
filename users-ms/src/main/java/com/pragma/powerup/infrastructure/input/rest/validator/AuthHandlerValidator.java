package com.pragma.powerup.infrastructure.input.rest.validator;

import com.pragma.powerup.domain.utils.Constants;
import com.pragma.powerup.infrastructure.input.rest.dto.LoginRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.RegisterRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthHandlerValidator {

    public Mono<RegisterRequest> validateRegisterRequest(RegisterRequest request) {
        return Mono.just(request)
                .flatMap(req -> RequestValidator.validateNotBlank(req.name(), "Nombre")
                        .thenReturn(req))
                .flatMap(req -> RequestValidator.validateNotBlank(req.email(), "Email")
                        .thenReturn(req))
                .flatMap(req -> RequestValidator.validateNotBlank(req.password(), "Contraseña")
                        .thenReturn(req))
                .flatMap(req -> RequestValidator.validateNotBlank(req.role(), Constants.E_ROLE)
                        .thenReturn(req));
    }

    public Mono<LoginRequest> validateLoginRequest(LoginRequest request) {
        return Mono.just(request)
                .flatMap(req -> RequestValidator.validateNotBlank(req.email(), "Email")
                        .thenReturn(req))
                .flatMap(req -> RequestValidator.validateNotBlank(req.password(), "Contraseña")
                        .thenReturn(req));
    }
}
