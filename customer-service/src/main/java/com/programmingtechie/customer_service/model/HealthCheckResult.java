package com.programmingtechie.customer_service.model;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "health_check_result")
@Entity
public class HealthCheckResult {
    @Id
    private String id;

    @Column(name = "category_name", columnDefinition = "TEXT")
    private String categoryName;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
