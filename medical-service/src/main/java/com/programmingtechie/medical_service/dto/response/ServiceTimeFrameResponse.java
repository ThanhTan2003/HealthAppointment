package com.programmingtechie.medical_service.dto.response;

import com.programmingtechie.medical_service.dto.response.Doctor.DoctorResponse;
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
    private Integer startTime;
    private Integer endTime;
    private Integer maximumQuantity;
    private Integer startNumber;
    private Integer endNumber;
    private Boolean isActive;
    private String status;
    private String doctorServiceId;
    private String roomId;
    private TimeFrameResponse timeFrameResponse;
    private DoctorResponse doctorResponse;
    private DoctorServiceResponse doctorServiceResponse;
    private RoomResponse roomResponse;
}
