package com.pragma.powerup.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BootcampRequest(
        @NotBlank @Size(max = 50) String name,
        @NotBlank @Size(max = 90) String description,
        @NotEmpty List<Long> capabilityIds
) {}
