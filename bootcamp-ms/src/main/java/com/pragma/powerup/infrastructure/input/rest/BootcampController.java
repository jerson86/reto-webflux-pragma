package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.handler.impl.IBootcampHandler;
import com.pragma.powerup.infrastructure.input.rest.dto.BootcampRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.BootcampResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bootcamps")
@RequiredArgsConstructor
@Slf4j
public class BootcampController {
    private final IBootcampHandler bootcampHandler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar bootcamp - HU4")
    public Mono<Void> save(@Valid @RequestBody BootcampRequest request) {
        return bootcampHandler.saveBootcamp(request);
    }

    @GetMapping
    public Mono<ResponseEntity<PageResponse<BootcampResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "true") boolean asc) {
        return bootcampHandler.getBootcamps(page, size, sortBy, asc)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return bootcampHandler.deleteBootcamp(id).log();
    }
}
