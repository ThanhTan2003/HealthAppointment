package com.programmingtechie.identity_service.dto.request.Customer;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAuthenticationRequest {
    private String userName;
    private String password;
}
