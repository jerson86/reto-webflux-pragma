package com.pragma.powerup.infrastructure.out.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("bootcamps")
@Data
public class BootcampEntity {
    @Id
    private Long id;
    private String name;
    private String description;
}