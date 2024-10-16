package com.programmingtechie.identity_service.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenResponse {
    private String email;
    private String token;
    private boolean isLogin;
}
