package com.programmingtechie.identity_service.dto.request;

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

    @Size(min = 3, message = "Tên đăng nhập phải ít nhất 3 ký tự")
    String userName;

    @Size(min = 8, message = "Mật khẩu phải ít nhất 8 ký tự")
    String password;

    String accountName;

    String status;

    LocalDateTime lastAccessTime;

    String accountTypeId;

    String doctorId;

    Set<String> roles;
}
