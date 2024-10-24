package com.programmingtechie.customer_service.model;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "administrative_units")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrativeUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "full_name_en", length = 255)
    private String fullNameEn;

    @Column(name = "short_name", length = 255)
    private String shortName;

    @Column(name = "short_name_en", length = 255)
    private String shortNameEn;

    @Column(name = "code_name", length = 255)
    private String codeName;

    @Column(name = "code_name_en", length = 255)
    private String codeNameEn;
}
