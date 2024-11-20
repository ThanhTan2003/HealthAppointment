package com.programmingtechie.medical_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTimeFrameUpdate {
    private Integer startTime;
    private Integer endTime;
    private Integer maximumQuantity;
    private Integer startNumber;
    private Integer endNumber;
    private String roomId;
}
