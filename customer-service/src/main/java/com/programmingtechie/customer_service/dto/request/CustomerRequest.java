package com.programmingtechie.customer_service.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String phoneNumber;

    private String email;

    private String password;

    private LocalDateTime lastUpdated;

}
