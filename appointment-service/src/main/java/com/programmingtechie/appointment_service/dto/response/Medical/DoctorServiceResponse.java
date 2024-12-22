package com.programmingtechie.appointment_service.dto.response.Medical;

import com.programmingtechie.appointment_service.dto.response.Doctor.DoctorResponse;

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
    private Boolean isActive;
    private Double unitPrice;

    private DoctorResponse doctorResponse;
}
