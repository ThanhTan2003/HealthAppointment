package com.programmingtechie.identity_service.dto.response.Customer;

import java.time.LocalDateTime;
import java.util.*;

import com.programmingtechie.identity_service.dto.response.Patient.PatientDetailsResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPatientResponse {
    private String id;
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String status;
    private LocalDateTime lastAccessTime;
    private LocalDateTime lastUpdated;
    private List<PatientDetailsResponse> customerDetail;
}
