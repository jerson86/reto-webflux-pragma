package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import com.pragma.powerup.domain.model.Capacity;
import com.pragma.powerup.domain.spi.ICapacityPersistencePort;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.CapacityTechnologyEntity;
import com.pragma.powerup.infrastructure.out.r2dbc.mapper.ICapacityEntityMapper;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.ICapacityRepository;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.ICapacityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CapacityR2dbcAdapter implements ICapacityPersistencePort {

    private final ICapacityRepository capacityRepository;
    private final ICapacityTechnologyRepository relationRepository;
    private final ICapacityEntityMapper mapper;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Capacity> save(Capacity capacity) {
        return capacityRepository.save(mapper.toEntity(capacity))
                .flatMap(savedEntity -> {
                    List<CapacityTechnologyEntity> relations = capacity.getTechnologyIds().stream()
                            .map(techId -> new CapacityTechnologyEntity(savedEntity.getId(), techId))
                            .toList();

                    return relationRepository.saveAll(relations)
                            .then(Mono.just(mapper.toDomain(savedEntity, capacity.getTechnologyIds())));
                })
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return capacityRepository.existsByName(name);
    }

    @Override
    public Flux<Capacity> findAll(int page, int size, String sortField, boolean ascending) {
        long offset = (long) page * size;
        return capacityRepository.findAllCustom(size, offset, sortField, ascending)
                .flatMap(entity ->
                        relationRepository.findAllByCapabilityId(entity.getId())
                                .map(rel -> rel.getTechnologyId())
                                .collectList()
                                .map(techIds -> {
                                    Capacity domain = mapper.toDomain(entity);
                                    domain.setTechnologyIds(techIds);
                                    return domain;
                                })
                );
    }

    @Override
    public Mono<Long> count() {
        return capacityRepository.count();
    }
}
