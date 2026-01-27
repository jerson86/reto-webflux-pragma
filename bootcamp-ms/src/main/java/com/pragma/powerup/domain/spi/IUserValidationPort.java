package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.AuthenticatedUser;
import reactor.core.publisher.Mono;

public interface IUserValidationPort {
    Mono<AuthenticatedUser> getAuthenticatedUser(String token);
}
