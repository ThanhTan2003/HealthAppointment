package com.programmingtechie.medical_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.medical_service.model.ServiceTimeFrame;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceTimeFrameRepository extends JpaRepository<ServiceTimeFrame, String> {

    @Query("SELECT stf FROM ServiceTimeFrame stf JOIN stf.doctorService ds WHERE ds.doctorId = :doctorId AND stf.dayOfWeek = :dayOfWeek ORDER BY stf.startTime ASC")
    List<ServiceTimeFrame> findByDoctorIdAndDayOfWeek(@Param("doctorId") String doctorId, @Param("dayOfWeek") String dayOfWeek);

    @Query("SELECT DISTINCT stf.doctorService.doctorId FROM ServiceTimeFrame stf")
    Page<String> findDistinctDoctorIds(Pageable pageable);

    @Query("SELECT DISTINCT stf.doctorService.service.specialtyId FROM ServiceTimeFrame stf")
    Page<String> findDistinctSpecialtyIds(Pageable pageable);

    @Query("SELECT DISTINCT stf.doctorService.service.serviceType.Id FROM ServiceTimeFrame stf")
    Page<String> findDistinctServiceTypeIds(Pageable pageable);

    // Phương thức tìm danh sách các ngày khám theo doctor_service_id và loại bỏ trùng lặp
    @Query("SELECT DISTINCT stf.dayOfWeek FROM ServiceTimeFrame stf WHERE stf.doctorService.id = :doctorServiceId")
    List<String> findListDayOfWeekByDoctorServiceId(@Param("doctorServiceId") String doctorServiceId);

    // Truy vấn tìm các ServiceTimeFrame theo doctorServiceId và dayOfWeek
    List<ServiceTimeFrame> findByDoctorServiceIdAndDayOfWeek(String doctorServiceId, String dayOfWeek);
}
