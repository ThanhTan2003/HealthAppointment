package com.programmingtechie.medical_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.programmingtechie.medical_service.model.Holiday;
import com.programmingtechie.medical_service.repository.HolidayRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayRepository holidayRepository;

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public List<Holiday> searchHolidayDays(String keyword) {
        return holidayRepository.searchHolidayDays(keyword);
    }

    public Holiday addHolidayDay(Holiday holidayDay) {
        // Kiểm tra tính hợp lệ của ngày, tháng
        validateHolidayDay(holidayDay);

        // Kiểm tra xem ngày nghỉ đã tồn tại chưa
        Optional<Holiday> existingHoliday =
                holidayRepository.findByDayAndMonth(holidayDay.getDay(), holidayDay.getMonth());
        if (existingHoliday.isPresent()) {
            throw new IllegalArgumentException("Ngày nghỉ này đã tồn tại!");
        }

        return holidayRepository.save(holidayDay);
    }

    public Holiday updateHolidayDay(Holiday holidayDay) {
        // Kiểm tra tính hợp lệ của ngày, tháng
        validateHolidayDay(holidayDay);

        // Kiểm tra xem ngày nghỉ có tồn tại để cập nhật không
        Optional<Holiday> existingHoliday =
                holidayRepository.findByDayAndMonth(holidayDay.getDay(), holidayDay.getMonth());
        if (existingHoliday.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy ngày nghỉ để cập nhật!");
        }

        // Cập nhật thông tin của ngày nghỉ hiện tại
        Holiday existing = existingHoliday.get();
        existing.setName(holidayDay.getName());

        return holidayRepository.save(existing);
    }

    public void deleteHolidayDay(Integer day, Integer month) {
        Holiday holidayDay = Holiday.builder().day(day).month(month).build();
        validateHolidayDay(holidayDay);

        Optional<Holiday> existingHoliday =
                holidayRepository.findByDayAndMonth(holidayDay.getDay(), holidayDay.getMonth());

        if (existingHoliday.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy ngày nghỉ để xóa!");
        }

        // Xóa ngày nghỉ nếu tìm thấy
        holidayRepository.delete(existingHoliday.get());
    }

    private void validateHolidayDay(Holiday holidayDay) {
        int day = holidayDay.getDay();
        int month = holidayDay.getMonth();

        // Kiểm tra tính hợp lệ của ngày theo tháng
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Tháng không hợp lệ!");
        }

        int maxDays =
                switch (month) {
                    case 4, 6, 9, 11 -> 30;
                    case 2 -> 28;
                    default -> 31;
                };

        if (day < 1 || day > maxDays) {
            throw new IllegalArgumentException("Ngày không hợp lệ cho tháng " + month + "!");
        }
    }
}
