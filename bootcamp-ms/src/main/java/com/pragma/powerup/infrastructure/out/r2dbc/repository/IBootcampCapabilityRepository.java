package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.BootcampCapabilityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IBootcampCapabilityRepository extends R2dbcRepository<BootcampCapabilityEntity, Long> {
    Flux<BootcampCapabilityEntity> findAllByBootcampId(Long bootcampId);

    @Query("SELECT COUNT(*) > 0 FROM bootcamp_capability WHERE capability_id = :capabilityId AND bootcamp_id != :excludeId")
    Mono<Boolean> existsByCapabilityIdAndBootcampIdNot(Long capabilityId, Long excludeId);

    Mono<Void> deleteByBootcampId(Long bootcampId);

    @Query("SELECT capability_id FROM bootcamp_capability WHERE bootcamp_id = :bootcampId")
    Flux<Long> findCapabilityIdsByBootcampId(Long bootcampId);
}
