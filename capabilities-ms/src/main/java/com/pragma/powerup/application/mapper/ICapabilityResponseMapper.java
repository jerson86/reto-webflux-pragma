package com.pragma.powerup.application.mapper;

import com.pragma.powerup.domain.model.TechnologyShort;
import com.pragma.powerup.infrastructure.input.rest.dto.CapabilityResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyShortResponse;
import org.mapstruct.Mapper;

import java.security.DrbgParameters;

@Mapper(componentModel = "spring")
public interface ICapabilityResponseMapper {

    CapabilityResponse toResponse(DrbgParameters.Capability capability);
    TechnologyShortResponse toShortResponse(TechnologyShort technologyShort);
}