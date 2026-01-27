package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IBootcampServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Bootcamp;
import com.pragma.powerup.domain.model.Capability;
import com.pragma.powerup.domain.spi.IBootcampNotificationPort;
import com.pragma.powerup.domain.spi.IBootcampPersistencePort;
import com.pragma.powerup.domain.spi.IExternalCapabilityServicePort;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class BootcampUseCase implements IBootcampServicePort {
    private final IBootcampPersistencePort persistencePort;
    private final IExternalCapabilityServicePort externalCapabilityPort;
    private final IBootcampNotificationPort notificationPort;
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public Mono<Void> saveBootcamp(Bootcamp bootcamp) {
        return persistencePort.existsByName(bootcamp.getName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new DomainException("El bootcamp ya existe"));
                    }

                    return externalCapabilityPort.verifyCapabilitiesExist(bootcamp.getCapabilityIds())
                            .flatMap(allExist -> {
                                if (Boolean.FALSE.equals(allExist)) {
                                    return Mono.error(new DomainException("Una o más capacidades no existen"));
                                }

                                return persistencePort.save(bootcamp);
                            });
                })
                .doOnSuccess(notificationPort::notifyBootcampCreation)
                .then();
    }

    @Override
    public Mono<PageResponse<Bootcamp>> getBootcamps(int page, int size, String sortBy, boolean asc) {
        return persistencePort.findAll(page, size, sortBy, asc)
                .collectList()
                .flatMap(bootcamps -> {
                    if (bootcamps.isEmpty()) return Mono.just(new PageResponse<>(List.of(), page, size, 0L, 0));

                    List<Long> allCapIds = bootcamps.stream()
                            .flatMap(b -> b.getCapabilityIds().stream())
                            .distinct().toList();

                    return externalCapabilityPort.getCapabilitiesWithTechs(allCapIds)
                            .collectMap(Capability::id)
                            .flatMap(capMap -> {
                                bootcamps.forEach(bootcamp -> {
                                    List<Capability> detailedCaps = bootcamp.getCapabilityIds().stream()
                                            .map(capMap::get)
                                            .filter(Objects::nonNull)
                                            .toList();
                                    bootcamp.setCapabilities(detailedCaps);
                                });

                                return persistencePort.count().map(total ->
                                        new PageResponse<>(bootcamps, page, size, total, (int) Math.ceil((double) total/size)));
                            });
                });
    }

    @Override
    public Mono<Void> deleteBootcamp(Long id) {
        return persistencePort.deleteBootcamp(id)
                .flatMap(capabilityIds ->
                        Flux.fromIterable(capabilityIds)
                                .concatMap(capId ->
                                        persistencePort.isCapabilityUsedInOtherBootcamps(capId, id)
                                                .flatMap(isUsed -> {
                                                    if (Boolean.FALSE.equals(isUsed)) {
                                                        return externalCapabilityPort.deleteCapability(capId)
                                                                .onErrorResume(e -> {
                                                                    logger.info("WARN: No se pudo borrar la capacidad externa " + capId);
                                                                    return Mono.empty();
                                                                });
                                                    }
                                                    return Mono.empty();
                                                })
                                ).then()
                );
    }
}