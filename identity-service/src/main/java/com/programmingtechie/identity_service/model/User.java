package com.programmingtechie.identity_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account", indexes = {
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

    private Set<String> roles;

    @ManyToOne
    @JoinColumn(name = "account_type_id", referencedColumnName = "id")
    private UserRole userRole;
}
