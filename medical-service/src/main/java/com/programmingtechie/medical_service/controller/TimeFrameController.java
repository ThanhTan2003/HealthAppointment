package com.programmingtechie.medical_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.medical_service.dto.request.TimeFrameRequest;
import com.programmingtechie.medical_service.dto.response.TimeFrameResponse;
import com.programmingtechie.medical_service.service.TimeFrameService;

import lombok.RequiredArgsConstructor;

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
    @PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc') or hasRole('NguoiDung')")
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
            @PathVariable String id, @RequestBody TimeFrameRequest request) {
        return ResponseEntity.ok(timeFrameService.updateTimeFrame(id, request));
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<Void> deleteTimeFrame(@PathVariable String id) {
        timeFrameService.deleteTimeFrame(id);
        return ResponseEntity.noContent().build();
    }
}
