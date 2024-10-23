package com.programmingtechie.identity_service.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    String userName;

    String password;

    String accountName;

    String status;

    String doctorId;

    String roleId;
}
