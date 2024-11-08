package com.programmingtechie.medical_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorServiceRequest {
    private String doctorId;
    private String serviceId;
    private String status;
}
