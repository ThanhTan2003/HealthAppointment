package com.programmingtechie.identity_service.mapper;

import java.time.Duration;
import java.time.LocalDateTime;

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
                .lastAccessTime(getTimeSinceLastAccess(user.getLastAccessTime()))
                .doctorId(user.getDoctorId())
                .roleId(user.getRole().getId())
                .roleName(user.getRole().getName())
                .build();
    }

    private String getTimeSinceLastAccess(LocalDateTime lastAccessTime) {
        if (lastAccessTime == null) return "Chưa sử dụng";
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(lastAccessTime, now);

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        long months = days / 30;
        long years = days / 365;

        if (seconds < 60) {
            return "Vừa truy cập";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days < 30) {
            return days + " ngày trước";
        } else if (months < 12) {
            return months + " tháng trước";
        } else {
            return years + " năm trước";
        }
    }
}
