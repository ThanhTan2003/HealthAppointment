package com.example.test_service.model;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "test_data")
@Entity
public class User {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "roles", nullable = false, length = 100)
    private Set<String> roles;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
