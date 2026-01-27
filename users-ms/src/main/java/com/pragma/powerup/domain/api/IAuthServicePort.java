package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.model.UserValidation;
import reactor.core.publisher.Mono;

public interface IAuthServicePort {
    Mono<User> register(User user);
    Mono<String> login(String email, String password);

    Mono<UserValidation> validateToken(String token);
}
