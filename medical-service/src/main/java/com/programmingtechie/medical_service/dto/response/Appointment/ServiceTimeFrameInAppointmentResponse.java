package com.programmingtechie.medical_service.dto.response.Appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTimeFrameInAppointmentResponse {
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

    private String timeFrameNameFullName;
    private String serviceName;
    private String doctorId;
    private String doctorQualificationName;
    private String doctorName;
    private String roomName;
}