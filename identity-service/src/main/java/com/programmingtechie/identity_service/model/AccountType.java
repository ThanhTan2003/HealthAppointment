package com.programmingtechie.identity_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account_type", indexes = {
        @Index(name = "idx_id", columnList = "id")
})
public class AccountType {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "TEXT")
    private String id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;
}
