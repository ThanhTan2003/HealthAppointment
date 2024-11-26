package com.programmingtechie.medical_service.mapper;

import org.springframework.stereotype.Component;

import com.programmingtechie.medical_service.dto.response.DoctorServiceResponse;
import com.programmingtechie.medical_service.dto.response.RoomResponse;
import com.programmingtechie.medical_service.dto.response.ServiceTimeFrameResponse;
import com.programmingtechie.medical_service.model.ServiceTimeFrame;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceTimeFrameMapper {
    private final DoctorServiceMapper doctorServiceMapper;
    private final RoomMapper roomMapper;
    private final TimeFrameMapper timeFrameMapper;

    public ServiceTimeFrameResponse toServiceTimeFrameResponse(ServiceTimeFrame serviceTimeFrame) {
        DoctorServiceResponse doctorServiceResponse =
                doctorServiceMapper.toDoctorServiceResponse(serviceTimeFrame.getDoctorService());
        RoomResponse roomResponse = roomMapper.toRoomResponse(serviceTimeFrame.getRoom());

        return ServiceTimeFrameResponse.builder()
                .id(serviceTimeFrame.getId())
                .dayOfWeek(serviceTimeFrame.getDayOfWeek())
                .startTime(serviceTimeFrame.getStartTime())
                .endTime(serviceTimeFrame.getEndTime())
                .maximumQuantity(serviceTimeFrame.getMaximumQuantity())
                .startNumber(serviceTimeFrame.getStartNumber())
                .endNumber(serviceTimeFrame.getEndNumber())
                .isActive(serviceTimeFrame.getIsActive())
                .status(serviceTimeFrame.getStatus())
                .doctorServiceId(serviceTimeFrame.getDoctorService().getId())
                .roomId(serviceTimeFrame.getRoom().getId())
                .timeFrameResponse(timeFrameMapper.toResponse(serviceTimeFrame.getTimeFrame()))
                .doctorServiceResponse(doctorServiceResponse)
                .roomResponse(roomResponse)
                .build();
    }
}
