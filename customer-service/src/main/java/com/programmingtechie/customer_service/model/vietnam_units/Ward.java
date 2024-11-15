package com.programmingtechie.customer_service.model.vietnam_units;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "wards")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ward {

    @Id
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "name_en", length = 255)
    private String nameEn;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "full_name_en", length = 255)
    private String fullNameEn;

    @Column(name = "code_name", length = 255)
    private String codeName;

    @ManyToOne
    @JoinColumn(name = "district_code", referencedColumnName = "code")
    private District district;

    @ManyToOne
    @JoinColumn(name = "administrative_unit_id", referencedColumnName = "id")
    private AdministrativeUnit administrativeUnit;
}
