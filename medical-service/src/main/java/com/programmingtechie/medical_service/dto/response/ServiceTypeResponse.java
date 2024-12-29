package com.programmingtechie.medical_service.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTypeResponse {
    private String id;
    private String name;
    private String image;
    private String description;
}
