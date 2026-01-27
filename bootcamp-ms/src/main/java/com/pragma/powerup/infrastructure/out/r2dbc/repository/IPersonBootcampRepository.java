package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.PersonBootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IPersonBootcampRepository extends ReactiveCrudRepository<PersonBootcampEntity, Long> {
    Mono<Long> countByBootcampId(Long bootcampId);
}
