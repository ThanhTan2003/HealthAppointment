package com.programmingtechie.medical_service.controller;

import com.programmingtechie.medical_service.dto.request.ServiceTypeRequest;
import com.programmingtechie.medical_service.dto.request.TimeFrameRequest;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.ServiceTypeResponse;
import com.programmingtechie.medical_service.dto.response.TimeFrameResponse;
import com.programmingtechie.medical_service.service.ServiceTypeService;
import com.programmingtechie.medical_service.service.TimeFrameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/medical/time-frame")
@RequiredArgsConstructor
public class TimeFrameController {
    private final TimeFrameService timeFrameService;

    @PostMapping("/create")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<TimeFrameResponse> createTimeFrame(@RequestBody TimeFrameRequest request) {
        return ResponseEntity.ok(timeFrameService.createTimeFrame(request));
    }

    @GetMapping("/public/id/{id}")
    public ResponseEntity<TimeFrameResponse> getTimeFrameById(@PathVariable String id) {
        return ResponseEntity.ok(timeFrameService.getTimeFrameById(id));
    }

    @GetMapping("/public/get-all")
    public ResponseEntity<List<TimeFrameResponse>> getAllTimeFrames() {
        return ResponseEntity.ok(timeFrameService.getAllTimeFrames());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<TimeFrameResponse> updateTimeFrame(
            @PathVariable String id,
            @RequestBody TimeFrameRequest request) {
        return ResponseEntity.ok(timeFrameService.updateTimeFrame(id, request));
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<Void> deleteTimeFrame(@PathVariable String id) {
        timeFrameService.deleteTimeFrame(id);
        return ResponseEntity.noContent().build();
    }
}
