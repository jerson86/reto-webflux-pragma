package com.pragma.powerup.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamp_capability")
@Data
@AllArgsConstructor
public class BootcampCapabilityEntity {
    private Long bootcampId;
    private Long capabilityId;
}
