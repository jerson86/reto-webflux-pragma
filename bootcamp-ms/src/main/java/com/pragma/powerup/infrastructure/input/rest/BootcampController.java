package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.handler.IBootcampHandler;
import com.pragma.powerup.infrastructure.input.rest.dto.BootcampRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.BootcampResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.SuccessfulBootcampResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bootcamps")
@RequiredArgsConstructor
@Slf4j
public class BootcampController {
    private final IBootcampHandler bootcampHandler;

    @PostMapping
    @Operation(summary = "Registrar bootcamp - HU4")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Void>> save(@Valid @RequestBody BootcampRequest request) {
        return bootcampHandler.saveBootcamp(request)
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
    }

    @GetMapping
    @Operation(summary = "Listar bootcamps - HU5")
    @PreAuthorize("hasRole('ADMIN')")
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
    @Operation(summary = "Borrar bootcamps - HU6")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> delete(@PathVariable Long id) {
        return bootcampHandler.deleteBootcamp(id).log();
    }

    @GetMapping("/most-successful")
    @Operation(summary = "Mostrar el bootcamp con mayor cantidad de personas - HU9")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<SuccessfulBootcampResponse>> getMostSuccessful() {
        return bootcampHandler.getMostSuccessfulBootcamp()
                .map(ResponseEntity::ok);
    }
}
