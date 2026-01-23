package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBootcampPersistencePort {
    Mono<Bootcamp> save(Bootcamp bootcamp);
    Mono<Boolean> existsByName(String name);
    Flux<Bootcamp> findAll(int page, int size, String sortField, boolean ascending);
    Mono<Long> count();
}
