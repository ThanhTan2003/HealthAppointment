package com.programmingtechie.identity_service.dto.request.Customer;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
    private String fullName;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String password;
    private String status;
}
