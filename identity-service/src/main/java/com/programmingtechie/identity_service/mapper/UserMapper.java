package com.programmingtechie.identity_service.mapper;

import org.springframework.stereotype.Component;

import com.programmingtechie.identity_service.dto.response.UserResponse;
import com.programmingtechie.identity_service.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .userName(user.getUserName())
                .password(user.getPassword())
                .accountName(user.getAccountName())
                .status(user.getStatus())
                .lastAccessTime(user.getLastAccessTime())
                .doctorId(user.getDoctorId())
                .roleId(user.getRole().getId())
                .roleName(user.getRole().getName())
                .build();
    }
}
