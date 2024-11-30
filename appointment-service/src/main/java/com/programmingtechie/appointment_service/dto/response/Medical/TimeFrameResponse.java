package com.programmingtechie.appointment_service.dto.response.Medical;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

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
