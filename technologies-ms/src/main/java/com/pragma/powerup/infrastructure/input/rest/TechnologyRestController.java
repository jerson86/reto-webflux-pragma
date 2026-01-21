package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.handler.impl.ITechnologyHandler;
import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/technologies")
@RequiredArgsConstructor
public class TechnologyRestController {
    private final ITechnologyHandler technologyHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar tecnología - HU1")
    public Mono<Void> save(@Valid @RequestBody TechnologyRequest request) {
        return technologyHandler.saveTechnology(request);
    }

    @GetMapping("/verify")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Contar tecnologias por IDs tecnología - HU2")
    public Mono<ResponseEntity<Long>> countByIds(@RequestParam List<Long> ids) {
        return technologyHandler.countByIds(ids)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
