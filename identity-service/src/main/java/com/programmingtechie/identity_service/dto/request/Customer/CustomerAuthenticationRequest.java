package com.programmingtechie.identity_service.dto.request.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAuthenticationRequest {
    private String userName; // userName có thể là email hoặc số điện thoại
    private String password;
}
