package com.programmingtechie.identity_service.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account_type")
public class Role {
    @Id
    String id;

    String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<User> users;
}
