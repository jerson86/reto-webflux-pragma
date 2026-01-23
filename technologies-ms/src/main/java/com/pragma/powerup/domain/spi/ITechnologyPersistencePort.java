package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.model.TechnologyShort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyPersistencePort {
    Mono<Technology> save(Technology technology);
    Mono<Boolean> existsByName(String name);
    Mono<Long> countByIds(List<Long> ids);
    Flux<TechnologyShort> findAllByIds(List<Long> ids);
}
