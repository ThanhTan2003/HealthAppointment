package com.programmingtechie.identity_service.dto.response.Doctor;

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
}
