package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.ICapacityServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Capacity;
import com.pragma.powerup.domain.model.TechnologyShort;
import com.pragma.powerup.domain.spi.ICapacityPersistencePort;
import com.pragma.powerup.domain.spi.IExternalTechnologyServicePort;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CapacityUseCase implements ICapacityServicePort {

    private final ICapacityPersistencePort capabilityPersistencePort;
    private final IExternalTechnologyServicePort externalTechnologyServicePort;

    @Override
    public Mono<Void> saveCapability(Capacity capability) {
        return capabilityPersistencePort.existsByName(capability.getName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DomainException("La capacidad ya existe"));
                    }

                    return externalTechnologyServicePort.allTechnologiesExist(capability.getTechnologyIds())
                            .flatMap(allExist -> {
                                if (allExist < capability.getTechnologyIds().size()) {
                                    return Mono.error(new DomainException("Una o más tecnologías no existen en el catálogo"));
                                }
                                return capabilityPersistencePort.save(capability);
                            });
                }).then();
    }

    @Override
    public Mono<PageResponse<Capacity>> getCapabilities(int page, int size, String sortField, boolean ascending) {
        return capabilityPersistencePort.findAll(page, size, sortField, ascending)
                .collectList()
                .flatMap(capabilities -> {
                    if (capabilities.isEmpty()) {
                        return Mono.just(new PageResponse<>(List.of(), page, size, 0, 0));
                    }

                    List<Long> allTechIds = capabilities.stream()
                            .flatMap(c -> c.getTechnologyIds().stream())
                            .distinct()
                            .toList();

                    return externalTechnologyServicePort.getTechnologiesByIds(allTechIds)
                            .collectMap(TechnologyShort::id)
                            .flatMap(techMap -> {
                                capabilities.forEach(cap -> {
                                    List<TechnologyShort> detailedTechs = cap.getTechnologyIds().stream()
                                            .map(techMap::get)
                                            .filter(Objects::nonNull)
                                            .toList();
                                    cap.setTechnologies(detailedTechs);
                                });

                                return capabilityPersistencePort.count().map(total ->
                                        new PageResponse<>(capabilities, page, size, total, (int) Math.ceil((double) total/size)));
                            });
                });
    }

    @Override
    public Mono<Boolean> verifyCapabilitiesExist(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Mono.just(Boolean.FALSE);
        }
        List<Long> uniqueIds = ids.stream().distinct().toList();
        return capabilityPersistencePort.countByIds(uniqueIds)
                .map(count -> count == uniqueIds.size());
    }

    @Override
    public Flux<Capacity> getCapabilitiesWithTechs(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Flux.empty();

        return capabilityPersistencePort.findAllByIds(ids)
                .flatMap(capability ->
                        capabilityPersistencePort.findAllByCapabilityId(capability.getId())
                                .map(Capacity::getTechnologyIds)
                                .flatMap(techIds ->
                                        externalTechnologyServicePort.getTechnologiesByIds(techIds)
                                                .collectList()
                                                .map(techs -> {
                                                    capability.setTechnologies(techs);
                                                    return capability;
                                                })
                                )
                );
    }
}
