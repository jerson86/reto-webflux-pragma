package com.pragma.powerup.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class Capacity {
    private final Long id;
    private final String name;
    private final String description;
    private final List<Long> technologyIds;
    private List<TechnologyShort> technologies;
}
