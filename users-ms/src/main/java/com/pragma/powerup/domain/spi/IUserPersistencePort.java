package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IUserPersistencePort {
    Mono<User> save(User user);
    Mono<User> findByEmail(String email);
    Flux<User> findAllByIds(List<Long> ids);
}
