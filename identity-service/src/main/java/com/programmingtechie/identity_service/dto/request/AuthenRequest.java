package com.programmingtechie.identity_service.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenRequest {
    private String email;
    private String password;
}
