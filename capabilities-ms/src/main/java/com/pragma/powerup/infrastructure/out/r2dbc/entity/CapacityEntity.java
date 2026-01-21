package com.pragma.powerup.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table(name = "capabilities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CapacityEntity {
    @Id
    private Long id;
    private String name;
    private String description;
    @Transient
    private List<Long> technologyIds;
}
