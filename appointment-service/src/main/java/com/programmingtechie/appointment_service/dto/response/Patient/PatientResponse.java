package com.programmingtechie.appointment_service.dto.response.Patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    private String id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String insuranceId;
    private String identificationCode;
    private String nation;
    private String occupation;
    private String phoneNumber;
    private String email;
    private String country;
    private String province;
    private String district;
    private String ward;
    private String address;
    private String relationship;
    private String note;
    private LocalDateTime lastUpdated;
    private String customerId;
}
