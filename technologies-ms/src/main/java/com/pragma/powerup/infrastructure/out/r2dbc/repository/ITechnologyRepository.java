package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ITechnologyRepository extends R2dbcRepository<TechnologyEntity, Long> {
    Mono<Boolean> existsByName(String name);
}
