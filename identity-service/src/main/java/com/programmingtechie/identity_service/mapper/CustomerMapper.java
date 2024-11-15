package com.programmingtechie.identity_service.mapper;

import com.programmingtechie.identity_service.dto.response.Customer.CustomerResponse;
import com.programmingtechie.identity_service.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomerMapper {
    public CustomerResponse toCustomerResponse(Customer customer)
    {
        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .status(customer.getStatus())
                .lastAccessTime(getTimeSinceLastAccess(customer.getLastAccessTime()))
                .lastUpdated(customer.getLastUpdated())
                .build();
    }

    private String getTimeSinceLastAccess(LocalDateTime lastAccessTime) {
        if(lastAccessTime == null)
            return "Chưa sử dụng";
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
