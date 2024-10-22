package com.programmingtechie.identity_service.dto.response.Customer;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String id;
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String status;
    private LocalDateTime lastAccessTime;
    private LocalDateTime lastUpdated;
}