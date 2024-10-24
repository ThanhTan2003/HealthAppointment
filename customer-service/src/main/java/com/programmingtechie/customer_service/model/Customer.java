package com.programmingtechie.customer_service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "customer")
@Entity
public class Customer {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "full_name", nullable = false, columnDefinition = "TEXT")
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 4)
    private String gender;

    // Số điện thoại cá nhân của tài khoản
    @Column(name = "phone_number", length = 15, unique = true)
    private String phoneNumber;

    // Email cá nhân của tài khoản
    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "last_access_time")
    private LocalDateTime lastAccessTime;

    @Column(name = "last_updated", nullable = true)
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "customerId")
    @ToString.Exclude
    @JsonManagedReference
    private List<Patient> patients;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.status == null) {
            this.status = "Đã kích hoạt";
        }
    }

    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
