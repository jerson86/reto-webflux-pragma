package com.pragma.powerup.infrastructure.out.r2dbc.mapper;

import com.pragma.powerup.domain.model.Capacity;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.CapacityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICapacityEntityMapper {
    CapacityEntity toEntity(Capacity capacity);
    @Mapping(target = "id", source = "capabilityEntity.id")
    @Mapping(target = "name", source = "capabilityEntity.name")
    @Mapping(target = "description", source = "capabilityEntity.description")
    @Mapping(target = "technologyIds", source = "techIds")
    Capacity toDomain(CapacityEntity capabilityEntity, List<Long> techIds);
}
