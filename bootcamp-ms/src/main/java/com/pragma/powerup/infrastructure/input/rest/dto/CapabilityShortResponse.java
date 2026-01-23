package com.pragma.powerup.infrastructure.input.rest.dto;

import java.util.List;

public record CapabilityShortResponse(
        Long id,
        String name,
        List<TechnologyShortResponse> technologies
) {}