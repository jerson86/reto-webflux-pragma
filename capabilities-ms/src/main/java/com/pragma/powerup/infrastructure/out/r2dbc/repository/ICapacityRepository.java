package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.CapacityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ICapacityRepository extends R2dbcRepository<CapacityEntity, Long> {
    Mono<Boolean> existsByName(String name);
    @Query("""
        SELECT c.*, COUNT(ct.technology_id) as tech_count 
        FROM capabilities c 
        LEFT JOIN capability_technology ct ON c.id = ct.capability_id 
        GROUP BY c.id 
        ORDER BY 
            CASE WHEN :sortField = 'name' AND :ascending = true THEN c.name END ASC,
            CASE WHEN :sortField = 'name' AND :ascending = false THEN c.name END DESC,
            CASE WHEN :sortField = 'tech_count' AND :ascending = true THEN COUNT(ct.technology_id) END ASC,
            CASE WHEN :sortField = 'tech_count' AND :ascending = false THEN COUNT(ct.technology_id) END DESC
        LIMIT :size OFFSET :offset
    """)
    Flux<CapacityEntity> findAllCustom(int size, long offset, String sortField, boolean ascending);
}
