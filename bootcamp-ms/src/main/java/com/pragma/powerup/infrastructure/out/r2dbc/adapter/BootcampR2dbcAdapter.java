package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import com.pragma.powerup.domain.model.Bootcamp;
import com.pragma.powerup.domain.spi.IBootcampPersistencePort;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.BootcampCapabilityEntity;
import com.pragma.powerup.infrastructure.out.r2dbc.mapper.IBootcampEntityMapper;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.IBootcampCapabilityRepository;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.IBootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BootcampR2dbcAdapter implements IBootcampPersistencePort {
    private final IBootcampRepository bootcampRepository;
    private final IBootcampEntityMapper mapper;
    private final IBootcampCapabilityRepository capabilityRepository;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Bootcamp> save(Bootcamp bootcamp) {
        return bootcampRepository.save(mapper.toEntity(bootcamp))
                .flatMap(savedEntity -> {
                    List<BootcampCapabilityEntity> relations = bootcamp.getCapabilityIds().stream()
                            .map(capId -> new BootcampCapabilityEntity(savedEntity.getId(), capId))
                            .toList();
                    return capabilityRepository.saveAll(relations)
                            .then(Mono.just(mapper.toDomain(savedEntity, bootcamp.getCapabilityIds())));
                })
                .as(transactionalOperator::transactional);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return bootcampRepository.existsByName(name);
    }

    @Override
    public Flux<Bootcamp> findAll(int page, int size, String sortField, boolean ascending) {
        long offset = (long) page * size;

        return bootcampRepository.findAllCustom(size, offset, sortField, ascending)
                .flatMap(entity ->
                        capabilityRepository.findAllByBootcampId(entity.getId())
                                .map(BootcampCapabilityEntity::getCapabilityId)
                                .collectList()
                                .map(capIds -> mapper.toDomain(entity, capIds))
                );
    }

    @Override
    public Mono<Long> count() {
        return bootcampRepository.count();
    }
}
