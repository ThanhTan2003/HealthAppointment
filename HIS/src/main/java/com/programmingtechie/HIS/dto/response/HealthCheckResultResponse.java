package com.programmingtechie.HIS.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
