package com.programmingtechie.doctor_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "doctor", indexes = {
        @Index(name = "idx_fullName", columnList = "fullName"),
        @Index(name = "idx_phone", columnList = "phoneNumber")
})
public class Doctor {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "full_name", nullable = false, columnDefinition = "TEXT")
    private String fullName;

    @Column(name = "gender", length = 4)
    private String gender;

    @Column(name = "phone_number", length = 10, unique = true)
    private String phoneNumber;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<DoctorLeave> doctorLeaves;

    // Mối quan hệ One-to-Many với bảng DoctorSpecialty
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private List<DoctorSpecialty> specialties;

    // Mối quan hệ One-to-Many với bảng DoctorQualification
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<DoctorQualification> doctorQualifications;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
