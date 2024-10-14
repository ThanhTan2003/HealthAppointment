package com.programmingtechie.doctor_service.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "specialty")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialty {
    @Id
    private String id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "specialty", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<DoctorSpecialty> doctors;

    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
