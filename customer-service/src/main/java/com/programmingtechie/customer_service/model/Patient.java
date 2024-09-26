package com.programmingtechie.customer_service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

public class Patient {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 4)
    private String gender;

    @Column(name = "insurance_id", nullable = false, length = 15)
    private String insuranceId;

    @Column(name = "identification_code_or_passport", nullable = false, length = 12)
    private String identificationCodeOrPassport;

    @Column(name = "nation", nullable = false, length = 100)
    private String nation;

    @Column(name = "occupation",nullable = false, length = 100)
    private String occupation;

    @Column(name = "phone_number", length = 10, unique = true)
    private String phoneNumber;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "country",nullable = false, length = 100)
    private String country;

    @Column(name = "province_or_city",nullable = false, length = 100)
    private String province;

    @Column(name = "district",nullable = false, length = 100)
    private String district;

    @Column(name = "ward_commune",nullable = false, length = 100)
    private String ward;

    @Column(name = "address",nullable = false, length = 100)
    private String address;

    @Column(name = "relationship",nullable = false, length = 30)
    private String relationship;

    @Column(name = "note", length = 50)
    private String note;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "customer_id",nullable = false, length = 36)
    private String customerId;

}
