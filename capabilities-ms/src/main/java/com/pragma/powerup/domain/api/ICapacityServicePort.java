package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Capacity;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityServicePort {
    Mono<Void> saveCapability(Capacity capacity);
    Mono<PageResponse<Capacity>> getCapabilities(int page, int size, String sortField, boolean ascending);
    Mono<Boolean> verifyCapabilitiesExist(List<Long> ids);
    Flux<Capacity> getCapabilitiesWithTechs(List<Long> ids);

    // CapabilityUseCase.java
    @Transactional
    Mono<Void> deleteCapability(Long id);
}
