package com.programmingtechie.identity_service.dto.response;

import com.programmingtechie.identity_service.dto.response.Doctor.DoctorResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userName;

    String password;

    String accountName;

    String roleId;

    String roleName;

    String status;

    String lastAccessTime;

    String doctorId;

    DoctorResponse doctorResponse;
}
