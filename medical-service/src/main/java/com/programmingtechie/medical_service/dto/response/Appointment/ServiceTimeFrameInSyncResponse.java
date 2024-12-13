package com.programmingtechie.medical_service.dto.response.Appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTimeFrameInSyncResponse {
    private String id;
    private String roomId;
    private String serviceId;
    private String doctorId;
}
