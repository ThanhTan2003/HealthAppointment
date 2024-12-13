package com.programmingtechie.appointment_service.dto.response.Medical;

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
