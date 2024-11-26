package com.programmingtechie.appointment_service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "appointment",
        indexes = {
            @Index(name = "idx_id", columnList = "id"),
            @Index(name = "idx_bill", columnList = "bill_id"),
            @Index(name = "idx_date", columnList = "date"),
            @Index(name = "idx_service_time_frame_date", columnList = "service_time_frame_id, date")
        })
public class Appointment {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status", columnDefinition = "TEXT")
    private String status;

    @Column(name = "order_number")
    private Integer orderNumber;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "service_time_frame_id", length = 36)
    private String serviceTimeFrameId;

    @Column(name = "medical_records_id", length = 36)
    private String medicalRecordsId;

    @Column(name = "patients_id", length = 36)
    private String patientsId;

    @Column(name = "replacement_doctor_id", length = 36)
    private String replacementDoctorId;

    @OneToOne
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    private Bill bill;

    @PrePersist
    private void ensureId() {
        if (this.lastUpdated == null) {
            this.lastUpdated = LocalDateTime.now();
        }
        if (this.dateTime == null) {
            this.dateTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
