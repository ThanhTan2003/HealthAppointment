package com.programmingtechie.medical_service.service;

import com.programmingtechie.medical_service.dto.request.TimeFrameRequest;
import com.programmingtechie.medical_service.dto.response.TimeFrameResponse;
import com.programmingtechie.medical_service.mapper.TimeFrameMapper;
import com.programmingtechie.medical_service.model.Holiday;
import com.programmingtechie.medical_service.model.TimeFrame;
import com.programmingtechie.medical_service.repository.HolidayRepository;
import com.programmingtechie.medical_service.repository.TimeFrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeFrameService {
    private final TimeFrameRepository timeFrameRepository;
    private final TimeFrameMapper timeFrameMapper;

    public TimeFrameResponse createTimeFrame(TimeFrameRequest request) {
        TimeFrame timeFrame = timeFrameMapper.toEntity(request);
        TimeFrame savedTimeFrame = timeFrameRepository.save(timeFrame);
        return timeFrameMapper.toResponse(savedTimeFrame);
    }

    public TimeFrameResponse getTimeFrameById(String id) {
        TimeFrame timeFrame = timeFrameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TimeFrame không tồn tại với ID: " + id));
        return timeFrameMapper.toResponse(timeFrame);
    }

    public List<TimeFrameResponse> getAllTimeFrames() {
        return timeFrameRepository.findAllByOrderByStartTimeAsc().stream()
                .map(timeFrameMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TimeFrameResponse updateTimeFrame(String id, TimeFrameRequest request) {
        TimeFrame existingTimeFrame = timeFrameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TimeFrame không tồn tại với ID: " + id));

        existingTimeFrame.setStartTime(request.getStartTime());
        existingTimeFrame.setEndTime(request.getEndTime());
        existingTimeFrame.setSession(request.getSession());

        TimeFrame updatedTimeFrame = timeFrameRepository.save(existingTimeFrame);
        return timeFrameMapper.toResponse(updatedTimeFrame);
    }

    public void deleteTimeFrame(String id) {
        if (!timeFrameRepository.existsById(id)) {
            throw new IllegalArgumentException("TimeFrame không tồn tại với ID: " + id);
        }
        timeFrameRepository.deleteById(id);
    }
}
