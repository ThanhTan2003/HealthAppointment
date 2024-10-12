package com.programmingtechie.medical_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private String id;
    private String name;
    private double unitPrice;
    private String description;
    private String status;
    private String serviceTypeId;
}
