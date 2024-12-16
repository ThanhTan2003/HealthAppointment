package com.programmingtechie.appointment_service.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "health_check_result")
public class HealthCheckResult {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false) // Đảm bảo có khóa ngoại tới Appointment
    private Appointment appointment;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file-name", nullable = false)
    private String fileName;

    @Column(name = "bucket-name", nullable = false)
    private String bucketName;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.lastUpdated == null) {
            this.lastUpdated = LocalDateTime.now();
        }
    }

    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
