package com.programmingtechie.medical_service.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.medical_service.model.Holiday;
import com.programmingtechie.medical_service.service.HolidayService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medical/holiday")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayDayService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        // Tạo body của response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false)); // đường dẫn của request

        // Trả về response với mã 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Lấy tất cả các ngày nghỉ
    @GetMapping("/get-all")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<List<Holiday>> getAllHolidays() {
        List<Holiday> holidays = holidayDayService.getAllHolidays();
        return new ResponseEntity<>(holidays, HttpStatus.OK);
    }

    // Tìm kiếm ngày nghỉ theo từ khóa
    @GetMapping("/search")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<List<Holiday>> searchHolidayDays(@RequestParam("keyword") String keyword) {
        List<Holiday> holidays = holidayDayService.searchHolidayDays(keyword);
        return new ResponseEntity<>(holidays, HttpStatus.OK);
    }

    // Thêm một ngày nghỉ mới
    @PostMapping("/create")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<Holiday> addHolidayDay(@RequestBody Holiday holidayDay) {
        Holiday createdHoliday = holidayDayService.addHolidayDay(holidayDay);
        return new ResponseEntity<>(createdHoliday, HttpStatus.CREATED);
    }

    // Cập nhật ngày nghỉ
    @PutMapping("/update")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<Holiday> updateHolidayDay(@RequestBody Holiday holidayDay) {
        Holiday updatedHoliday = holidayDayService.updateHolidayDay(holidayDay);
        return new ResponseEntity<>(updatedHoliday, HttpStatus.OK);
    }

    // Xóa một ngày nghỉ
    @DeleteMapping("/delete/{day}/{month}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<Void> deleteHolidayDay(
            @PathVariable("day") Integer day, @PathVariable("month") Integer month) {
        holidayDayService.deleteHolidayDay(day, month);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
