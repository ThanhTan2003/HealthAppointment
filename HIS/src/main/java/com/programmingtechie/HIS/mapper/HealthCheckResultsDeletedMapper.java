package com.programmingtechie.HIS.mapper;

import com.programmingtechie.HIS.dto.response.HealthCheckResultsDeletedResponse;
import com.programmingtechie.HIS.model.HealthCheckResultsDeleted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HealthCheckResultsDeletedMapper {

    public HealthCheckResultsDeletedResponse toHealthCheckResultsDeletedResponse(HealthCheckResultsDeleted healthCheckResultsDeleted)
    {
        return HealthCheckResultsDeletedResponse.builder()
                .id(healthCheckResultsDeleted.getId())
                .lastUpdated(healthCheckResultsDeleted.getLastUpdated())
                .build();
    }
}
