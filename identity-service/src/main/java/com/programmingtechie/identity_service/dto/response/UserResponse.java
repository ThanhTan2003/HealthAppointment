package com.programmingtechie.identity_service.dto.response;

import com.programmingtechie.identity_service.model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

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

    LocalDateTime lastAccessTime;

    String doctorId;

}
