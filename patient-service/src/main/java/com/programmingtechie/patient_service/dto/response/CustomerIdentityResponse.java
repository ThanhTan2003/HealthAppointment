package com.programmingtechie.patient_service.dto.response;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerIdentityResponse {
    private String id;
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String status;
    private String lastAccessTime;
    private LocalDateTime lastUpdated;
}
