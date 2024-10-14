package com.programmingtechie.identity_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "account",
        indexes = {
            @Index(name = "idx_userName", columnList = "userName"),
            @Index(name = "idx_status", columnList = "status"),
        })
public class User {
    @Id
    @Column(name = "user_name", nullable = false, columnDefinition = "TEXT")
    private String userName;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "account_name", nullable = false, columnDefinition = "TEXT")
    private String accountName;

    @Column(name = "status", nullable = false, columnDefinition = "TEXT")
    private String status;

    @Column(name = "last_access_time")
    private LocalDateTime lastAccessTime;

    @Column(name = "doctor_id", length = 36)
    private String doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
