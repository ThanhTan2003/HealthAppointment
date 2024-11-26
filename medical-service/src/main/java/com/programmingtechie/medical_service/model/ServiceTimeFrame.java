package com.programmingtechie.medical_service.model;

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
@Table(name = "service_time_frame")
public class ServiceTimeFrame {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @Column(name = "start_time")
    private Integer startTime;

    @Column(name = "end_time")
    private Integer endTime;

    @Column(name = "maximum_quantity")
    private Integer maximumQuantity;

    @Column(name = "start_number")
    private Integer startNumber;

    @Column(name = "end_number")
    private Integer endNumber;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "status", length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "time_frame_id")
    private TimeFrame timeFrame;

    @ManyToOne
    @JoinColumn(name = "doctor_service_id")
    private DoctorService doctorService;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

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
