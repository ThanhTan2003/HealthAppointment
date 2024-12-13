package com.programmingtechie.HIS.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
            @Index(name = "idx_date", columnList = "date"),
        })
public class Appointment {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "status")
    private String status;

    @Column(name = "order_number")
    private Integer orderNumber;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "doctor_service_id", length = 36)
    private String doctorServiceId;

    @Column(name = "medical_records_id", length = 36)
    private String medicalRecordsId;

    @Column(name = "patients_id", length = 36)
    private String patientsId;

    @Column(name = "customer_id", length = 36)
    private String customerId;

    @Column(name = "replacement_doctor_id")
    private String replacementDoctorId;

    // Mối quan hệ One-to-Many với HealthCheckResult
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HealthCheckResult> healthCheckResults;

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
