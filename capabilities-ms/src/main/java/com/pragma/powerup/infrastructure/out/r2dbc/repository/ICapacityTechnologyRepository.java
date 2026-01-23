package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.CapacityTechnologyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ICapacityTechnologyRepository extends R2dbcRepository<CapacityTechnologyEntity, Long> {
    Flux<CapacityTechnologyEntity> findAllByCapabilityId(Long capabilityId);
    @Query("SELECT technology_id FROM capability_technology WHERE capability_id = :capabilityId")
    Flux<Long> findTechnologyIdsByCapabilityId(Long capabilityId);

    @Query("SELECT COUNT(*) > 0 FROM capability_technology WHERE technology_id = :technologyId AND capability_id != :excludeId")
    Mono<Boolean> existsByTechnologyIdAndCapabilityIdNot(Long technologyId, Long excludeId);

    Mono<Void> deleteByCapabilityId(Long capabilityId);
}
