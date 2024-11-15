package com.programmingtechie.medical_service.dto.request.Room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityRequest {
    private String dayOfWeek;
    private Integer startTime;
    private Integer endTime;
    private String function;
    private String keyword;
    private int page = 1;
    private int size = 10;
}
