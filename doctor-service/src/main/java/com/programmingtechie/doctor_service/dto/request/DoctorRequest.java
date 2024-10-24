package com.programmingtechie.doctor_service.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorRequest {
    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String identificationCode;

    private String phoneNumber;

    private String email;

    private String provinceOrCity;

    private String district;

    private String wardOrCommune;

    private String address;

    private String education;

    private String qualificationId;

    private String position;

    private String description;

    private String status;

    private String image;

    private String roomId;
}
