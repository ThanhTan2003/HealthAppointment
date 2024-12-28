package com.programmingtechie.medical_service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.medical_service.model.ServiceTimeFrame;

public interface ServiceTimeFrameRepository extends JpaRepository<ServiceTimeFrame, String> {

    @Query(
            "SELECT stf FROM ServiceTimeFrame stf JOIN stf.doctorService ds WHERE ds.doctorId = :doctorId AND stf.dayOfWeek = :dayOfWeek")
    List<ServiceTimeFrame> findByDoctorIdAndDayOfWeek(
            @Param("doctorId") String doctorId, @Param("dayOfWeek") String dayOfWeek);

    @Query("SELECT DISTINCT stf.doctorService.doctorId FROM ServiceTimeFrame stf")
    Page<String> findDistinctDoctorIds(Pageable pageable);

    @Query("SELECT DISTINCT stf.doctorService.service.specialtyId " + "FROM ServiceTimeFrame stf "
            + "WHERE stf.doctorService.service.specialtyId IS NOT NULL")
    Page<String> findDistinctSpecialtyIds(Pageable pageable);

    @Query("SELECT DISTINCT stf.doctorService.service.serviceType.Id FROM ServiceTimeFrame stf")
    Page<String> findDistinctServiceTypeIds(Pageable pageable);

    // Phương thức tìm danh sách các ngày khám theo doctor_service_id và loại bỏ trùng lặp
    @Query("SELECT DISTINCT stf.dayOfWeek FROM ServiceTimeFrame stf WHERE stf.doctorService.id = :doctorServiceId")
    List<String> findListDayOfWeekByDoctorServiceId(@Param("doctorServiceId") String doctorServiceId);

    // Truy vấn tìm các ServiceTimeFrame theo doctorServiceId và dayOfWeek
    @Query("SELECT stf FROM ServiceTimeFrame stf " + "WHERE stf.doctorService.id = :doctorServiceId "
            + "AND stf.dayOfWeek = :dayOfWeek "
            + "AND stf.isActive = :isActive "
            + "AND unaccent(LOWER(stf.status)) = unaccent(LOWER('nhan dang ky'))" +
            " ORDER BY stf.timeFrame.startTime ASC")
    List<ServiceTimeFrame> findByDoctorServiceIdAndDayOfWeek(
            String doctorServiceId, String dayOfWeek, Boolean isActive);

    @Query("SELECT stf FROM ServiceTimeFrame stf " + "WHERE stf.doctorService.id = :doctorServiceId "
            + "AND stf.dayOfWeek = :dayOfWeek "
            + "AND stf.isActive = :isActive "
            + "AND unaccent(LOWER(stf.status)) = unaccent(LOWER('nhan dang ky')) "
            + "AND (stf.lastUpdated >= :oneMonthAgo) " +
            " ORDER BY stf.timeFrame.startTime ASC")
    List<ServiceTimeFrame> findByDoctorServiceIdAndDayOfWeek(
            String doctorServiceId, String dayOfWeek, Boolean isActive, LocalDateTime oneMonthAgo);

    // Kiểm tra sự tồn tại dựa trên ID, isActive = true, và status = "Nhận đăng ký"
    @Query(
            "SELECT COUNT(stf) > 0 FROM ServiceTimeFrame stf WHERE stf.id = :id AND stf.isActive = true AND unaccent(LOWER(stf.status)) = unaccent(LOWER('nhan dang ky'))")
    boolean existsByIdAndIsActiveAndStatus(@Param("id") String id);

    @Query("SELECT stf FROM ServiceTimeFrame stf WHERE stf.doctorService.id = :id and stf.isActive = true")
    List<ServiceTimeFrame> getByDoctorServiceId(String id);
}
