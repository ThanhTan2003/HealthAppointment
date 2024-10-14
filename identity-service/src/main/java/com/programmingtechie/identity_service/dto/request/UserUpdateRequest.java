package com.programmingtechie.identity_service.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

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

    String doctorId;

    List<String> roles;
}
