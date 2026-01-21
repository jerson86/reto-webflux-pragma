package com.pragma.powerup.infrastructure.out.r2dbc.mapper;

import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.TechnologyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ITechnologyEntityMapper {
    TechnologyEntity toEntity(Technology technology);
    Technology toDomain(TechnologyEntity entity);
}
