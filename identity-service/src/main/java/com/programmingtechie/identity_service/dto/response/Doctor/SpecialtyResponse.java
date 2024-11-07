package com.programmingtechie.identity_service.dto.response.Doctor;

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
