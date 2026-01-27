package com.pragma.powerup.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IEnrollmentPersistencePort {
    Mono<Void> saveAll(Long personId, List<Long> bootcampIds);
}
