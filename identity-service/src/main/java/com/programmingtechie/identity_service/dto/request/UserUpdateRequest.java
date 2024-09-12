package com.programmingtechie.identity_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String userName;

    String password;

    String accountName;

    String status;

    LocalDateTime lastAccessTime;

    String accountTypeId;

    String doctorId;
}
