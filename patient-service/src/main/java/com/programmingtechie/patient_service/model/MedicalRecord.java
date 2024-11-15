package com.programmingtechie.patient_service.model;

import java.time.*;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "medical_record")
@Entity
public class MedicalRecord {
    @Id
    @Column(nullable = false, length = 30)
    private String id;

    @Column(name = "doctor_name", nullable = false, length = 100)
    private String doctorName;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "note", length = 50)
    private String note;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // @Column(name = "patient_id",nullable = false, length = 36)
    // private String patientId;

}
