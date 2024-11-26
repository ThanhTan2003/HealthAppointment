package com.programmingtechie.medical_service.dto.response;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeFrameResponse {
    private String id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String session;
    private String name;
    private String fullName;
}
