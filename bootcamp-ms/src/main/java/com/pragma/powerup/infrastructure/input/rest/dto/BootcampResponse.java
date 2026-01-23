package com.pragma.powerup.infrastructure.input.rest.dto;

import java.util.List;

public record BootcampResponse(
        Long id,
        String name,
        String description,
        List<CapabilityShortResponse> capabilities
) {}
