package com.programmingtechie.medical_service.mapper;

import org.springframework.stereotype.Component;

import com.programmingtechie.medical_service.dto.response.RoomResponse;
import com.programmingtechie.medical_service.model.Room;

@Component
public class RoomMapper {
    public RoomResponse toRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .location(room.getLocation())
                .function(room.getFunction())
                .status(room.getStatus())
                .build();
    }
}
