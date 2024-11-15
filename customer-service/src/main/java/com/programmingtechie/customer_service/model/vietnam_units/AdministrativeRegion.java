package com.programmingtechie.customer_service.model.vietnam_units;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "administrative_regions")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrativeRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "name_en", nullable = false, length = 255)
    private String nameEn;

    @Column(name = "code_name", length = 255)
    private String codeName;

    @Column(name = "code_name_en", length = 255)
    private String codeNameEn;
}
