package com.programmingtechie.medical_service.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class HolidayId implements Serializable {
    private Integer day;
    private Integer month;
}
