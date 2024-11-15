package com.programmingtechie.patient_service.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OccupationRequest {
    private String id;

    private String name;
}
