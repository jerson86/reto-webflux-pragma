package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.model.TechnologyShort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyServicePort {
    Mono<Void> saveTechnology(Technology technology);
    Mono<Long> verifyTechnologiesExist(List<Long> ids);
    Flux<TechnologyShort> findAllByIds(List<Long> ids);
}
