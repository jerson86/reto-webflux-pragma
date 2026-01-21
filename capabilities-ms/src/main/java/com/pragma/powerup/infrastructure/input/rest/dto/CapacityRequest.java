package com.pragma.powerup.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapacityRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no debe superar los 50 caracteres")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 90, message = "La descripción no debe superar los 90 caracteres")
    private String description;

    @NotEmpty(message = "Debe asociar al menos una tecnología")
    private List<Long> technologyIds;
}
