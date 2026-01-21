package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.CapacityTechnologyEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ICapacityTechnologyRepository extends R2dbcRepository<CapacityTechnologyEntity, Long> {
    Flux<CapacityTechnologyEntity> findAllByCapabilityId(Long capabilityId);
}
