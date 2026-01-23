package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Capacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityPersistencePort {
    Mono<Capacity> save(Capacity capacity);
    Mono<Boolean> existsByName(String name);
    Flux<Capacity> findAll(int page, int size, String sortField, boolean ascending);
    Mono<Long> count();
    Mono<Long> countByIds(List<Long> ids);
    Flux<Capacity> findAllByIds(List<Long> ids);
    Flux<Capacity> findAllByCapabilityId(Long id);
    Mono<List<Long>> deleteById(Long id);
    Mono<Boolean> isTechnologyUsedInOtherCapabilities(Long technologyId, Long excludeCapabilityId);
}
