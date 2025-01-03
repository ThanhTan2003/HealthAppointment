package com.programmingtechie.doctor_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SpecialtyResponse {
    private String specialtyId;
    private String specialtyName;
    private String description;
    private String image;
}
