package com.pragma.powerup.application.mapper;

import com.pragma.powerup.domain.model.Bootcamp;
import com.pragma.powerup.domain.model.Capability;
import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.infrastructure.input.rest.dto.BootcampResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.CapabilityShortResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyShortResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IBootcampResponseMapper {
    BootcampResponse toResponse(Bootcamp bootcamp);
    CapabilityShortResponse toCapabilityResponse(Capability capability);
    TechnologyShortResponse toTechnologyResponse(Technology technology);
}