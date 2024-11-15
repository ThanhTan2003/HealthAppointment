package com.programmingtechie.customer_service.model.vietnam_units;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "provinces")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Province {
    @Id
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "name_en", length = 255)
    private String nameEn;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "full_name_en", length = 255)
    private String fullNameEn;

    @Column(name = "code_name", length = 255)
    private String codeName;

    @ManyToOne
    @JoinColumn(name = "administrative_unit_id", referencedColumnName = "id")
    private AdministrativeUnit administrativeUnit;

    @ManyToOne
    @JoinColumn(name = "administrative_region_id", referencedColumnName = "id")
    private AdministrativeRegion administrativeRegion;
}
