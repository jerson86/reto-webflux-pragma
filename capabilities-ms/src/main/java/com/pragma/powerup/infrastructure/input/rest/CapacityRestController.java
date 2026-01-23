package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.handler.impl.ICapacityHandler;
import com.pragma.powerup.infrastructure.input.rest.dto.CapabilityResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.CapacityRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar capacidades - HU3")
    public Mono<PageResponse<CapabilityResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "true") boolean asc
    ) {
        return capacityHandler.getCapabilities(page, size, sortBy, asc);
    }

    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar capacidades por ids - HU4")
    public Mono<ResponseEntity<Boolean>> verifyCapabilities(@RequestParam List<Long> ids) {
        return capacityHandler.verifyCapabilitiesExist(ids)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/list-by-ids")
    public Flux<CapabilityResponse> getCapabilitiesWithTechs(@RequestParam List<Long> ids) {
        return capacityHandler.getCapabilitiesWithTechs(ids);
    }
}
