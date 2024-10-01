package com.programmingtechie.customer_service.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.*;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private String id;

    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String phoneNumber;

    private String email;

    private String password;

    private String status;

    private LocalDateTime lastUpdated;

    private List<PatientResponse> patient;
}
