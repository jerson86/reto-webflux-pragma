package com.pragma.powerup.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EnrollmentRequest(
        @NotEmpty
        @Size(min = 1, max = 5, message = "Puedes inscribirte en un mínimo de 1 y un máximo de 5 bootcamps por solicitud")
        List<Long> bootcampIds
) {}
