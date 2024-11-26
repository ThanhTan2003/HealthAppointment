package com.programmingtechie.medical_service.mapper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.programmingtechie.medical_service.dto.request.TimeFrameRequest;
import com.programmingtechie.medical_service.dto.response.TimeFrameResponse;
import com.programmingtechie.medical_service.model.TimeFrame;

@Component
public class TimeFrameMapper {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    public TimeFrame toEntity(TimeFrameRequest request) {
        return TimeFrame.builder()
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .session(request.getSession())
                .build();
    }

    public TimeFrameResponse toResponse(TimeFrame timeFrame) {
        TimeFrameResponse response = new TimeFrameResponse();
        response.setId(timeFrame.getId());
        response.setStartTime(timeFrame.getStartTime());
        response.setEndTime(timeFrame.getEndTime());
        response.setSession(timeFrame.getSession());

        String formattedName = formatTimeFrameName(timeFrame.getStartTime(), timeFrame.getEndTime());
        response.setName(formattedName);

        String formattedFullName =
                formatTimeFrameFullName(timeFrame.getStartTime(), timeFrame.getEndTime(), timeFrame.getSession());
        response.setFullName(formattedFullName);

        return response;
    }

    private String formatTimeFrameName(LocalTime startTime, LocalTime endTime) {
        String start = startTime.format(TIME_FORMATTER);
        String end = endTime.format(TIME_FORMATTER);
        return String.format("%s - %s", start, end);
    }

    private String formatTimeFrameFullName(LocalTime startTime, LocalTime endTime, String session) {
        String start = startTime.format(TIME_FORMATTER);
        String end = endTime.format(TIME_FORMATTER);
        return String.format("%s - %s (%s)", start, end, session);
    }
}
