package com.programmingtechie.medical_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTimeFrameRequest {
    private String dayOfWeek;
    private Integer maximumQuantity;
    private Integer startNumber;
    private Integer endNumber;
    private String doctorServiceId;
    private String roomId;
    private Boolean isActive;
    private String timeFrameId;
}
