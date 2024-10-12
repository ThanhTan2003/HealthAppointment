package com.programmingtechie.medical_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoomResponse {
    private String id;
    private String name;
    private String location;
    private String function;
    private String status;
}
