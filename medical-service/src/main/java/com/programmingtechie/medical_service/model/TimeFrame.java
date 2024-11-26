package com.programmingtechie.medical_service.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "time_frame")
public class TimeFrame {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_ime")
    private LocalTime endTime;

    @Column(name = "session")
    private String session;

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
}
