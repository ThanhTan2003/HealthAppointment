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
public class HealthCheckResultsDeletedResponse {
    private String id;

    private LocalDateTime lastUpdated;
}
