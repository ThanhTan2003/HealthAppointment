package com.programmingtechie.patient_service.dto.response;

import java.time.LocalDateTime;
import java.util.*;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerWithPatientDetailsResponse {
    private String id;
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String status;
    private String lastAccessTime;
    private LocalDateTime lastUpdated;
    private PageResponse<PatientResponse> patientDetails;
}
