package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Technology;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITechnologyServicePort {
    Mono<Void> saveTechnology(Technology technology);
    Mono<Long> verifyTechnologiesExist(List<Long> ids);
}
