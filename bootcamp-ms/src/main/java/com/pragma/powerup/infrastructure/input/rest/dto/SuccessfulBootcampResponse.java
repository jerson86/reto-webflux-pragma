package com.pragma.powerup.infrastructure.input.rest.dto;

import com.pragma.powerup.domain.model.Capability;
import lombok.Data;

import java.util.List;

@Data
public class SuccessfulBootcampResponse {
    private Long id;
    private String name;
    private String description;
    private List<InrolledPerson> enrolledPersons;
    private List<Capability> capabilities;
}
