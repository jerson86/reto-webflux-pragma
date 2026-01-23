package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootcampPersistencePort {
    Mono<Bootcamp> save(Bootcamp bootcamp);
    Mono<Boolean> existsByName(String name);
    Flux<Bootcamp> findAll(int page, int size, String sortField, boolean ascending);
    Mono<Long> count();
    Mono<List<Long>> deleteBootcamp(Long bootcampId);
    Mono<Boolean> isCapabilityUsedInOtherBootcamps(Long capabilityId, Long excludeBootcampId);
}
