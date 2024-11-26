package com.programmingtechie.medical_service.dto.request;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeFrameRequest {
    private LocalTime startTime;

    private LocalTime endTime;

    private String session;
}
