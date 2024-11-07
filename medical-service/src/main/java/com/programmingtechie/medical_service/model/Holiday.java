package com.programmingtechie.medical_service.model;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "holiday")
@IdClass(HolidayId.class)
public class Holiday {
    @Id
    @Column(name = "day", nullable = false)
    private Integer day;

    @Id
    @Column(name = "month", nullable = false)
    private Integer month;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;
}
