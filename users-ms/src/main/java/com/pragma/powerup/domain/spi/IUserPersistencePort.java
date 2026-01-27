package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.User;
import reactor.core.publisher.Mono;

public interface IUserPersistencePort {
    Mono<User> save(User user);
    Mono<User> findByEmail(String email);
}
