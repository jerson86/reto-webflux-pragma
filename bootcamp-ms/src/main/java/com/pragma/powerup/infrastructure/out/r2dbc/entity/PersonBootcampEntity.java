package com.pragma.powerup.infrastructure.out.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("person_bootcamp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonBootcampEntity {
    @Id
    private Long id;
    private Long personId;
    private Long bootcampId;
}
