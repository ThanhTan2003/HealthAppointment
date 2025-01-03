package com.programmingtechie.medical_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.medical_service.model.DoctorService;

public interface DoctorServiceRepository extends JpaRepository<DoctorService, String> {
    Page<DoctorService> findAll(Pageable pageable);

    boolean existsByDoctorIdAndServiceId(String doctorId, String serviceId);

    // Kiểm tra xem DoctorService có tồn tại với doctorId và serviceId
    DoctorService findByDoctorIdAndServiceId(String doctorId, String serviceId);

    // Kiểm tra xem DoctorService có tồn tại với doctorId và serviceId và isActive = true
    boolean existsByDoctorIdAndServiceIdAndIsActiveTrue(String doctorId, String serviceId);

    // Kiểm tra xem DoctorService có tồn tại với doctorId và serviceId và isActive = false
    boolean existsByDoctorIdAndServiceIdAndIsActiveFalse(String doctorId, String serviceId);

    //Page<DoctorService> findByDoctorId(String doctorId, Pageable pageable);

    Page<DoctorService> findByDoctorIdAndIsActiveTrue(String doctorId, Pageable pageable);

    // Tìm danh sách DoctorService với phân trang
    Page<DoctorService> findByServiceId(String serviceId, Pageable pageable);

    @Query("SELECT ds FROM DoctorService ds " + "WHERE ds.service.id = :serviceId "
            + "AND EXISTS ("
            + "  SELECT 1 FROM ServiceTimeFrame stf "
            + "  WHERE stf.doctorService.id = ds.id"
            + ")")
    Page<DoctorService> findByDoctorServiceExistsInServiceTimeFrameByServiceId(
            @Param("serviceId") String serviceId, Pageable pageable);

    @Query("SELECT ds FROM DoctorService ds " + "WHERE ds.doctorId = :doctorId "
            + "AND EXISTS ("
            + "  SELECT 1 FROM ServiceTimeFrame stf "
            + "  WHERE stf.doctorService.id = ds.id"
            + ")")
    Page<DoctorService> findByDoctorServiceExistsInServiceTimeFrame(
            @Param("doctorId") String doctorId, Pageable pageable);
}
