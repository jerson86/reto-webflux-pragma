package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.TechnologyShort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IExternalTechnologyServicePort {
    Mono<Long> allTechnologiesExist(List<Long> ids);
    Flux<TechnologyShort> getTechnologiesByIds(List<Long> ids);
    Mono<Void> deleteTechnology(Long techId);
}
