package com.programmingtechie.patient_service.dto.request;

import java.time.LocalDate;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientCreationRequest {
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
    private String customerId;
}
