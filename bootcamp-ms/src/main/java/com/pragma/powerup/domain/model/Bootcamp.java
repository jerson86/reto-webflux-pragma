package com.pragma.powerup.domain.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Bootcamp {
    private final Long id;
    private final String name;
    private final String description;
    private LocalDate startDate;
    private Integer durationDays;
    private final List<Long> capabilityIds;
    private List<Capability> capabilities;
}
