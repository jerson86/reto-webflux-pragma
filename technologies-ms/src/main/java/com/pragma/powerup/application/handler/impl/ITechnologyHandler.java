package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyRequest;
import reactor.core.publisher.Mono;

public interface ITechnologyHandler {
    Mono<Void> saveTechnology(TechnologyRequest technologyRequest);
}
