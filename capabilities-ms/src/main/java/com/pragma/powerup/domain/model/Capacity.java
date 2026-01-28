package com.pragma.powerup.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Capacity {
    private final Long id;
    private final String name;
    private final String description;
    private final List<Long> technologyIds;
    private List<TechnologyShort> technologies;
}
