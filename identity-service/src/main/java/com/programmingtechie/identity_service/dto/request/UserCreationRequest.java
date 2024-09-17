package com.programmingtechie.identity_service.dto.request;

import com.programmingtechie.identity_service.dto.response.RoleResponse;
import com.programmingtechie.identity_service.model.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    String userName;

    String password;

    String accountName;

    String status;

    String doctorId;

    String roleId;
}
