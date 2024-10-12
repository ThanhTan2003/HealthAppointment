package com.programmingtechie.medical_service.controller;

import com.programmingtechie.medical_service.model.Holiday;
import com.programmingtechie.medical_service.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medical/holiday")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayDayService;

    // Lấy tất cả các ngày nghỉ
    @GetMapping("/get-all")
    public ResponseEntity<List<Holiday>> getAllHolidays() {
        List<Holiday> holidays = holidayDayService.getAllHolidays();
        return new ResponseEntity<>(holidays, HttpStatus.OK);
    }

    // Tìm kiếm ngày nghỉ theo từ khóa
    @GetMapping("/search")
    public ResponseEntity<List<Holiday>> searchHolidayDays(@RequestParam("keyword") String keyword) {
        List<Holiday> holidays = holidayDayService.searchHolidayDays(keyword);
        return new ResponseEntity<>(holidays, HttpStatus.OK);
    }

    // Thêm một ngày nghỉ mới
    @PostMapping("/create")
    public ResponseEntity<Holiday> addHolidayDay(@RequestBody Holiday holidayDay) {
        Holiday createdHoliday = holidayDayService.addHolidayDay(holidayDay);
        return new ResponseEntity<>(createdHoliday, HttpStatus.CREATED);
    }

    // Cập nhật ngày nghỉ
    @PutMapping("/update")
    public ResponseEntity<Holiday> updateHolidayDay(@RequestBody Holiday holidayDay) {
        Holiday updatedHoliday = holidayDayService.updateHolidayDay(holidayDay);
        return new ResponseEntity<>(updatedHoliday, HttpStatus.OK);
    }

    // Xóa một ngày nghỉ
    @DeleteMapping("/delete/{day}/{month}")
    public ResponseEntity<Void> deleteHolidayDay(@PathVariable("day") Integer day, @PathVariable("month") Integer month) {
        holidayDayService.deleteHolidayDay(day, month);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
