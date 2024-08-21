package com.programmingtechie.doctor_service.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DoctorResponse
{
    private String id;

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

    private LocalDateTime lastUpdated;

    private String roomId;
}
