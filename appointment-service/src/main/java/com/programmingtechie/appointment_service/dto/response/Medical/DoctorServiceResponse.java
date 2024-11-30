package com.programmingtechie.appointment_service.dto.response.Medical;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorServiceResponse {
    private String id;
    private String doctorId;
    private String serviceId;
    private String roomId;
    private Boolean isActive;
    private Double unitPrice;
}
