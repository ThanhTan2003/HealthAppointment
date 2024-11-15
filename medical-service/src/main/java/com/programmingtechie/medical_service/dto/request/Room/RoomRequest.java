package com.programmingtechie.medical_service.dto.request.Room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    private String id;
    private String name;
    private String location;
    private String function;
    private String status;
}
