package com.programmingtechie.identity_service.dto.response;

import java.time.LocalDateTime;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
     String userName;

    String password;

    String accountName;

    String roleId;

    String roleName;

    String status;

    LocalDateTime lastAccessTime;

    String doctorId;
}
