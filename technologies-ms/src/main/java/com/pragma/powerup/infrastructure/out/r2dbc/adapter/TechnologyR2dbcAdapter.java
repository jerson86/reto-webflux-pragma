package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.model.TechnologyShort;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import com.pragma.powerup.infrastructure.out.r2dbc.mapper.ITechnologyEntityMapper;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.ITechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TechnologyR2dbcAdapter implements ITechnologyPersistencePort {
    private final ITechnologyRepository repository;
    private final ITechnologyEntityMapper mapper;

    @Override
    public Mono<Technology> save(Technology technology) {
        return repository.save(mapper.toEntity(technology))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Mono<Long> countByIds(List<Long> ids) {
        return repository.findAllById(ids).count();
    }

    @Override
    public Flux<TechnologyShort> findAllByIds(List<Long> ids) {
        return repository.findAllById(ids)
                .map(entity -> new TechnologyShort(entity.getId(), entity.getName()));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }
}
