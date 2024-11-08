package com.programmingtechie.medical_service.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

import lombok.*;

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
