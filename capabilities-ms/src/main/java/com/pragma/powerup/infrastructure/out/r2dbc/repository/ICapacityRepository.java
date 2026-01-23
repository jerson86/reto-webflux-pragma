package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.CapacityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ICapacityRepository extends R2dbcRepository<CapacityEntity, Long> {
    Mono<Boolean> existsByName(String name);
    @Query("""
        SELECT c.* FROM capabilities c
        LEFT JOIN capability_technology ct ON c.id = ct.capability_id
        GROUP BY c.id
        ORDER BY 
            CASE WHEN :sortBy = 'name' AND :asc = true THEN c.name END ASC,
            CASE WHEN :sortBy = 'name' AND :asc = false THEN c.name END DESC,
            CASE WHEN :sortBy = 'tech_count' AND :asc = true THEN COUNT(ct.technology_id) END ASC,
            CASE WHEN :sortBy = 'tech_count' AND :asc = false THEN COUNT(ct.technology_id) END DESC
        LIMIT :size OFFSET :offset
    """)
    Flux<CapacityEntity> findAllCustom(int size, long offset, String sortField, boolean ascending);

    Mono<Long> countByIdIn(List<Long> ids);

    Flux<CapacityEntity> findAllByIdIn(List<Long> ids);
}
