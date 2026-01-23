package com.pragma.powerup.infrastructure.out.r2dbc.mapper;

import com.pragma.powerup.domain.model.Bootcamp;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.BootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IBootcampEntityMapper {
    @Mapping(target = "id", source = "bootcamp.id")
    @Mapping(target = "name", source = "bootcamp.name")
    @Mapping(target = "description", source = "bootcamp.description")
    BootcampEntity toEntity(Bootcamp bootcamp);

    @Mapping(target = "capabilityIds", source = "capabilityIds")
    @Mapping(target = "capabilities", ignore = true)
    Bootcamp toDomain(BootcampEntity entity, List<Long> capabilityIds);
}
