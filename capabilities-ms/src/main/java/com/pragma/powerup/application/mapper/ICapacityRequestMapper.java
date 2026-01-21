package com.pragma.powerup.application.mapper;

import com.pragma.powerup.domain.model.Capacity;
import com.pragma.powerup.infrastructure.input.rest.dto.CapacityRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ICapacityRequestMapper {

    @Mapping(target = "id", ignore = true)
    Capacity toTechnology(CapacityRequest request);
}
