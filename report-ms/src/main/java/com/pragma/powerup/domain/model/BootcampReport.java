package com.pragma.powerup.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BootcampReport {
    private Long bootcampId;
    private String name;
    private int capacityCount;
    private int technologyCount;
    private long enrolledCount;
    private LocalDateTime registrationDate;
}
