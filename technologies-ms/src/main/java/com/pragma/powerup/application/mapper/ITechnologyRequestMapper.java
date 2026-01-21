package com.pragma.powerup.application.mapper;

import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ITechnologyRequestMapper {

    @Mapping(target = "id", ignore = true)
    Technology toTechnology(TechnologyRequest request);
}
