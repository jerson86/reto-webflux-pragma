package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ITechnologyServicePort;
import com.pragma.powerup.domain.exception.AlreadyExistsException;
import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import reactor.core.publisher.Mono;

import java.util.List;

public class TechnologyUseCase implements ITechnologyServicePort {
    private final ITechnologyPersistencePort persistencePort;

    public TechnologyUseCase(ITechnologyPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Void> saveTechnology(Technology technology) {
        return persistencePort.existsByName(technology.getName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new AlreadyExistsException("La tecnología ya existe"));
                    }
                    return persistencePort.save(technology);
                })
                .then();
    }

    @Override
    public Mono<Long> verifyTechnologiesExist(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Mono.just(Long.MIN_VALUE);
        }

        List<Long> uniqueIds = ids.stream().distinct().toList();

        return persistencePort.countByIds(uniqueIds);
    }
}
