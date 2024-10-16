package com.programmingtechie.identity_service.dto.response;

import java.util.Set;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String password;
    private Set<String> roles;
}
