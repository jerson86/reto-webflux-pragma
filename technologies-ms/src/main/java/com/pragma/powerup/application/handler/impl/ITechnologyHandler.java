package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyShortResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyHandler {
    Mono<Void> saveTechnology(TechnologyRequest technologyRequest);
    Mono<Long> countByIds(List<Long> ids);
    Flux<TechnologyShortResponse> findAllByIds(List<Long> ids);

    Mono<Void> deleteTechnology(Long id);
}
