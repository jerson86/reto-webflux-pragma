package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Technology;
import reactor.core.publisher.Mono;

public interface ITechnologyServicePort {
    Mono<Void> saveTechnology(Technology technology);
}
