package com.programmingtechie.appointment_service.dto.response.His;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResultResponse {
    private String id;

    private String appointmentId;

    private String name;

    private String fileName;

    private String bucketName;

    private LocalDateTime lastUpdated;

    private String URL;
}
