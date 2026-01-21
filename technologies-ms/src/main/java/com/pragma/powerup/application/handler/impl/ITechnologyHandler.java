package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyHandler {
    Mono<Void> saveTechnology(TechnologyRequest technologyRequest);
    Mono<Long> countByIds(List<Long> ids);
}
