package com.programmingtechie.doctor_service.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SpecialtyResponse {
    private String specialtyId;
    private String specialtyName;
    private String description;
}
