package com.programmingtechie.medical_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DoctorResponse {
    private String id;

    private String fullName;

    private String gender;

    private String phoneNumber;

    private String description;

    private String status;
}
