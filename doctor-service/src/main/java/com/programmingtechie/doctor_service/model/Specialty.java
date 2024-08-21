package com.programmingtechie.doctor_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "specialty")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialty
{
    @Id
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
