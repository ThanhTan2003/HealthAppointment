package com.programmingtechie.doctor_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "qualification")
public class Qualification {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(nullable = false, length = 20)
    private String abbreviation;

    @Column(nullable = false, length = 50)
    private String name;
}
