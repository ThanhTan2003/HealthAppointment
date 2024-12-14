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
public class HealthCheckResultsDeletedResponse {
    private String id;

    private LocalDateTime lastUpdated;
}
