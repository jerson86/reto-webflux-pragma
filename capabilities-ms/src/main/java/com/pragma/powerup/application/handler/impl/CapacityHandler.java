package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.mapper.ICapacityRequestMapper;
import com.pragma.powerup.domain.api.ICapacityServicePort;
import com.pragma.powerup.infrastructure.input.rest.dto.CapabilityResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.CapacityRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CapacityHandler implements ICapacityHandler {
    private final ICapacityServicePort capacityServicePort;
    private final ICapacityRequestMapper capacityRequestMapper;

    @Override
    public Mono<Void> saveCapacity(CapacityRequest capacityRequest) {
        return Mono.just(capacityRequest)
                .map(capacityRequestMapper::toTechnology)
                .flatMap(capacityServicePort::saveCapability);
    }

    @Override
    public Mono<PageResponse<CapabilityResponse>> getCapabilities(int page, int size, String sortBy, boolean asc) {
        return capacityServicePort.getCapabilities(page, size, sortBy, asc)
                .map(pageResponse -> {
                    List<CapabilityResponse> content = pageResponse.content().stream()
                            .map(capabilityResponseMapper::toResponse)
                            .toList();
                    return new PageResponse<>(content, pageResponse.page(), pageResponse.size(),
                            pageResponse.totalElements(), pageResponse.totalPages());
                });
    }
}
