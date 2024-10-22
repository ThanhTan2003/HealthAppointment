package com.programmingtechie.doctor_service.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QualificationResponse {
    private String abbreviation;
    private String name;
    private int displayOrder;
}