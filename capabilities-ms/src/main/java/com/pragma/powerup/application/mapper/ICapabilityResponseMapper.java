package com.pragma.powerup.application.mapper;

import com.pragma.powerup.domain.model.Capacity;
import com.pragma.powerup.domain.model.TechnologyShort;
import com.pragma.powerup.infrastructure.input.rest.dto.CapabilityResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyShortResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICapabilityResponseMapper {

    CapabilityResponse toResponse(Capacity capability);
    TechnologyShortResponse toShortResponse(TechnologyShort technologyShort);
}