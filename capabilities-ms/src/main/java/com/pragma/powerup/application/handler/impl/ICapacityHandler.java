package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.infrastructure.input.rest.dto.CapabilityResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.CapacityRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import reactor.core.publisher.Mono;

public interface ICapacityHandler {
    Mono<Void> saveCapacity(CapacityRequest capacityRequest);
    Mono<PageResponse<CapabilityResponse>> getCapabilities(int page, int size, String sortBy, boolean asc);
}
