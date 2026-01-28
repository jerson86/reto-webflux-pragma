package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.PersonBootcampEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPersonBootcampRepository extends ReactiveCrudRepository<PersonBootcampEntity, Long> {
    Mono<Long> countByBootcampId(Long bootcampId);

    @Query("SELECT bootcamp_id FROM person_bootcamp GROUP BY bootcamp_id ORDER BY COUNT(person_id) DESC LIMIT 1")
    Mono<Long> findTopBootcampId();

    @Query("SELECT person_id FROM person_bootcamp WHERE bootcamp_id = :bootcampId")
    Flux<Long> findPersonIdsByBootcampId(Long bootcampId);
}
