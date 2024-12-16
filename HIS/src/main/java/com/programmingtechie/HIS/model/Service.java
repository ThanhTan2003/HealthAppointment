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
@Table(name = "service")
public class Service {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "specialty_id", length = 20)
    private String specialtyId;

    @ManyToOne
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

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
