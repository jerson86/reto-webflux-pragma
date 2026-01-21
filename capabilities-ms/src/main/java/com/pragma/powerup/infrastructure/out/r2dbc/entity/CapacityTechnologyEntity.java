package com.pragma.powerup.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Table("capability_technology")
@Data
@AllArgsConstructor
public class CapacityTechnologyEntity {
    private Long capabilityId;
    private Long technologyId;
}
