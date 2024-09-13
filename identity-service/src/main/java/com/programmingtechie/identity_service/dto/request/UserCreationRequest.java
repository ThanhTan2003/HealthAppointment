package com.programmingtechie.identity_service.dto.request;

import jakarta.persistence.Column;
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

    LocalDateTime lastAccessTime;

    String accountTypeId;

    String doctorId;

    Set<String> roles;
}
