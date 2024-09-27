package com.programmingtechie.doctor_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "qualification")
public class Qualification {
    @Id
    @Column(nullable = false, columnDefinition = "TEXT")
    private String abbreviation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(nullable = false)
    private int displayOrder;

    // Mối quan hệ One-to-Many với bảng DoctorQualification
    @OneToMany(mappedBy = "qualification", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<DoctorQualification> doctorQualifications;
}
