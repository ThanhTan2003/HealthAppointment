package com.programmingtechie.patient_service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(
        name = "patient",
        indexes = {@Index(name = "idx_id", columnList = "id")})
@Entity
public class Patient {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "full_name", nullable = false, columnDefinition = "TEXT")
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 4)
    private String gender;

    @Column(name = "insurance_id", nullable = false, length = 15)
    private String insuranceId;

    @Column(name = "identification_code", nullable = false, length = 12)
    private String identificationCode;

    @Column(name = "nation", nullable = false, columnDefinition = "TEXT")
    private String nation;

    @Column(name = "occupation", nullable = false, columnDefinition = "TEXT")
    private String occupation;

    // Số điện thoại liên hệ với người có thông tin trong hồ sơ
    @Column(name = "phone_number", nullable = false, length = 10, unique = true)
    private String phoneNumber;

    // Email liên hệ với người có thông tin trong hồ sơ
    @Column(name = "email", nullable = false, columnDefinition = "TEXT")
    private String email;

    @Column(name = "country", nullable = false, columnDefinition = "TEXT")
    private String country;

    @Column(name = "province", nullable = false, columnDefinition = "TEXT")
    private String province;

    @Column(name = "district", nullable = false, columnDefinition = "TEXT")
    private String district;

    @Column(name = "ward", nullable = false, columnDefinition = "TEXT")
    private String ward;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "relationship", nullable = true, columnDefinition = "TEXT")
    private String relationship;

    @Column(name = "note", nullable = true, columnDefinition = "TEXT")
    private String note;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
