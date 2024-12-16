package com.programmingtechie.appointment_service.dto.response.His;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResultsDeletedResponse {
    private String id;

    private LocalDateTime lastUpdated;
}
