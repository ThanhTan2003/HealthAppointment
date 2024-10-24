package com.programmingtechie.identity_service.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "date_of_birth")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    @Column(name = "gender", length = 4)
    private String gender; // Giới tính: nam/nữ

    // Số điện thoại cá nhân của tài khoản
    @Column(name = "phone_number", length = 10, unique = true)
    private String phoneNumber;

    // Email cá nhân của tài khoản
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_type_id", nullable = false)
    private Role role;

    @Column(name = "last_access_time")
    private LocalDateTime lastAccessTime;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.status == null) {
            this.status = "Đang hoạt động";
        }
    }

    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
