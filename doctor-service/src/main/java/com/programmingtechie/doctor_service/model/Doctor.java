package com.programmingtechie.doctor_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 4)
    private String gender;

    @Column(name = "identification_code", length = 12, unique = true)
    private String identificationCode;

    @Column(name = "phone_number", length = 10, unique = true)
    private String phoneNumber;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "province_or_city", length = 30)
    private String provinceOrCity;

    @Column(name = "district", length = 30)
    private String district;

    @Column(name = "ward_or_commune", length = 30)
    private String wardOrCommune;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "education", length = 20)
    private String education;

    @Column(name = "qualification", length = 30)
    private String qualification;

    @Column(name = "postion", length = 30)
    private String position;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "image", columnDefinition = "text")
    private String image;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "room_id", length = 36)
    private String roomId;

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
