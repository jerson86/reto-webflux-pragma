package com.pragma.powerup.infrastructure.input.rest.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record BootcampRequest(
        @NotBlank
        @Size(max = 50)
        String name,

        @NotBlank
        @Size(max = 90)
        String description,

        @NotNull
        @FutureOrPresent
        LocalDate startDate,

        @Min(1)
        Integer durationDays,

        @NotEmpty
        @Size(min = 1, max = 4, message = "El bootcamp debe tener entre 1 y 4 capacidades")
        List<Long> capabilityIds
) {}
