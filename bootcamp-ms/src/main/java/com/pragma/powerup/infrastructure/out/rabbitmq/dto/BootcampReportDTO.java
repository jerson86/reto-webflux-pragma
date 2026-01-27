package com.pragma.powerup.infrastructure.out.rabbitmq.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BootcampReportDTO implements Serializable {
    private Long bootcampId;
    private String name;
    private int capacityCount;
    private int technologyCount;
    private long enrolledCount;
}
