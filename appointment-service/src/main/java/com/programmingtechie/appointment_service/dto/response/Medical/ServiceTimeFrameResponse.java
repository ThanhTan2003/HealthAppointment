package com.programmingtechie.appointment_service.dto.response.Medical;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTimeFrameResponse {
    private String id;
    private String dayOfWeek;
    private Integer maximumQuantity;
    private Integer startNumber;
    private Integer endNumber;
    private Boolean isActive;
    private String status;
    private String doctorServiceId;
    private String roomId;
    private TimeFrameResponse timeFrameResponse;
    private DoctorServiceResponse doctorServiceResponse;
    private RoomResponse roomResponse;
}
