package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.BootcampCapabilityEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IBootcampCapabilityRepository extends R2dbcRepository<BootcampCapabilityEntity, Long> {
    Flux<BootcampCapabilityEntity> findAllByBootcampId(Long bootcampId);
}
