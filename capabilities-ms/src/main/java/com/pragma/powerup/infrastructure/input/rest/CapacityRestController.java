package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.handler.impl.ICapacityHandler;
import com.pragma.powerup.infrastructure.input.rest.dto.CapabilityResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.CapacityRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/capabilities")
@RequiredArgsConstructor
public class CapacityRestController {
    private final ICapacityHandler capacityHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear una nueva capacidad con sus tecnologías - HU2")
    public Mono<Void> save(@Valid @RequestBody CapacityRequest request) {
        return capacityHandler.saveCapacity(request);
    }

    @GetMapping
    public Mono<PageResponse<CapabilityResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "true") boolean asc
    ) {
        return capacityHandler.getCapabilities(page, size, sortBy, asc);
    }
}
