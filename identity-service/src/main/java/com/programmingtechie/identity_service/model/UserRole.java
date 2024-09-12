package com.programmingtechie.identity_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account_type", indexes = {
        @Index(name = "idx_id", columnList = "id")
})
public class UserRole {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "TEXT")
    private String id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @OneToMany(mappedBy = "userRole")
    private List<User> users = new ArrayList<>();
}
