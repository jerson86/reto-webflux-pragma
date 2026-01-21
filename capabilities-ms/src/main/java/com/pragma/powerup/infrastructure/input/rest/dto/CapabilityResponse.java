package com.pragma.powerup.infrastructure.input.rest.dto;

import java.util.List;

public record CapabilityResponse(
        Long id,
        String name,
        String description,
        List<TechnologyShortResponse> technologies
) {}
