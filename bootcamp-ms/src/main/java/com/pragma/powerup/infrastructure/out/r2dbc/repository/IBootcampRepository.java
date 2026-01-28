package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.BootcampEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface IBootcampRepository extends R2dbcRepository<BootcampEntity, Long> {
    Mono<Boolean> existsByName(String name);

    @Query("""
        SELECT b.* FROM bootcamps b
        LEFT JOIN bootcamp_capability bc ON b.id = bc.bootcamp_id
        GROUP BY b.id
        ORDER BY 
            CASE WHEN :sortBy = 'name' AND :asc = true THEN b.name END ASC,
            CASE WHEN :sortBy = 'name' AND :asc = false THEN b.name END DESC,
            CASE WHEN :sortBy = 'cap_count' AND :asc = true THEN COUNT(bc.capability_id) END ASC,
            CASE WHEN :sortBy = 'cap_count' AND :asc = false THEN COUNT(bc.capability_id) END DESC
        LIMIT :size OFFSET :offset
    """)
    Flux<BootcampEntity> findAllCustom(int size, long offset, String sortBy, boolean asc);

    @Query("SELECT b.* FROM bootcamps b " +
            "INNER JOIN person_bootcamp pb ON b.id = pb.bootcamp_id " +
            "WHERE pb.person_id = :personId")
    Flux<BootcampEntity> findAllByPersonId(Long personId);

    Flux<BootcampEntity> findAllByIdIn(List<Long> ids);
}
