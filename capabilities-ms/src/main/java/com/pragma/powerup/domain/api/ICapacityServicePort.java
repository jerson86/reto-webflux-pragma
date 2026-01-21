package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Capacity;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import reactor.core.publisher.Mono;

public interface ICapacityServicePort {
    Mono<Void> saveCapability(Capacity capacity);

    Mono<PageResponse<Capacity>> getCapabilities(int page, int size, String sortField, boolean ascending);
}
