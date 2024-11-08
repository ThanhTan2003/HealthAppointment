package com.programmingtechie.customer_service.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OccupationRequest {
    private String id;

    private String name;
}
