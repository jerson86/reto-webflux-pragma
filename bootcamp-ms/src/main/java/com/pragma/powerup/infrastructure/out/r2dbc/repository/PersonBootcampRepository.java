package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.PersonBootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PersonBootcampRepository extends ReactiveCrudRepository<PersonBootcampEntity, Long> {
}
