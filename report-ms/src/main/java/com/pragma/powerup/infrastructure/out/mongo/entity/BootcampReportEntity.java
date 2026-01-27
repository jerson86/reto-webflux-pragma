package com.pragma.powerup.infrastructure.out.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "bootcamp_reports")
@Data
public class BootcampReportEntity {
    @Id
    private String id;
    private Long bootcampId;
    private String name;
    private int capacityCount;
    private int technologyCount;
    private long enrolledCount;
    private LocalDateTime registrationDate;
}