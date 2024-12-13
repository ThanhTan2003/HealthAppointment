package com.programmingtechie.HIS.dto.response;

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
