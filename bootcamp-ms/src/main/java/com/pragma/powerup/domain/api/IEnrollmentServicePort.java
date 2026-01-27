package com.pragma.powerup.domain.api;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IEnrollmentServicePort {
    Mono<Void> enrollPerson(Long personId, List<Long> bootcampIds);
}
