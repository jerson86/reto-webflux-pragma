package com.pragma.powerup.application.mapper;

import com.pragma.powerup.domain.model.Bootcamp;
import com.pragma.powerup.infrastructure.input.rest.dto.BootcampRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IBootcampRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "capabilities", ignore = true)
    @Mapping(target = "technologyCount", ignore = true)
    Bootcamp toDomain(BootcampRequest request);
}
