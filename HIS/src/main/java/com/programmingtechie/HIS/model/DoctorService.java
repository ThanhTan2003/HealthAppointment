package com.programmingtechie.HIS.model;

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
@Table(name = "doctor_service")
public class DoctorService {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "doctor_id", length = 20)
    private String doctorId;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "unit_price")
    private Double unitPrice;

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
