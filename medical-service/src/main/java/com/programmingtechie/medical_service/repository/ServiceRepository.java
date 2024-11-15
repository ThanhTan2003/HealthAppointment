package com.programmingtechie.medical_service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.medical_service.model.Service;

public interface ServiceRepository extends JpaRepository<Service, String> {

    @Query("SELECT s FROM Service s")
    Page<Service> getAllService(Pageable pageable);

    // Tìm kiếm dịch vụ theo từ khoá với phân trang
    @Query(
            value = "SELECT * FROM service s WHERE "
                    + "unaccent(LOWER(s.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(s.status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    Page<Service> searchServices(@Param("keyword") String keyword, Pageable pageable);

    // Tìm kiếm dịch vụ theo ServiceTypeId với phân trang
    @Query("SELECT s FROM Service s WHERE s.serviceType.id = :serviceTypeId")
    Page<Service> findByServiceTypeId(@Param("serviceTypeId") String serviceTypeId, Pageable pageable);
    // Lưu ý: Trong HQL, sẽ sử dụng tên thuộc tính serviceType chứ không phải tên cột service_type_id

    // Tìm kiếm dịch vụ theo SpecialtyId với phân trang
    @Query("SELECT s FROM Service s WHERE s.specialtyId = :specialtyId")
    Page<Service> findBySpecialtyId(@Param("specialtyId") String specialtyId, Pageable pageable);

    @Query(
            value = "SELECT * FROM service s WHERE s.specialty_id IN :specialtyIds "
                    + "AND NOT EXISTS (SELECT 1 FROM doctor_service ds WHERE ds.service_id = s.id AND ds.doctor_id = :doctorId) "
                    + "AND (unaccent(LOWER(s.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                    + "OR unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))))",
            nativeQuery = true)
    Page<Service> findServicesBySpecialtyIdsAndNotAssignedToDoctorAndKeyword(
            @Param("specialtyIds") List<String> specialtyIds,
            @Param("doctorId") String doctorId,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query(
            value = "SELECT * FROM service s WHERE s.specialty_id = :specialtyId "
                    + "AND s.id NOT EXISTS (SELECT 1 FROM doctor_service ds WHERE ds.doctor_id = :doctorId) "
                    + "AND (unaccent(LOWER(s.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                    + "OR unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))))",
            nativeQuery = true)
    Page<Service> findServicesBySpecialtyIdAndNotAssignedToDoctorAndKeyword(
            @Param("specialtyId") String specialtyId,
            @Param("doctorId") String doctorId,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query(
            value = "SELECT * FROM service s WHERE "
                    + "NOT EXISTS (SELECT 1 FROM doctor_service ds WHERE ds.service_id = s.id AND ds.doctor_id = :doctorId) "
                    + "AND (unaccent(LOWER(s.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                    + "OR unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))))",
            nativeQuery = true)
    Page<Service> findServicesWithServiceTypeNotNullAndNotAssignedToDoctor(
            @Param("doctorId") String doctorId,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query(
            value = "SELECT * FROM service s WHERE s.service_type_id = :serviceTypeId "
                    + "AND NOT EXISTS (SELECT 1 FROM doctor_service ds WHERE ds.service_id = s.id AND ds.doctor_id = :doctorId) "
                    + "AND (unaccent(LOWER(s.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                    + "OR unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))))",
            nativeQuery = true)
    Page<Service> findServicesWithServiceTypeIdNotNullAndNotAssignedToDoctor(
            @Param("doctorId") String doctorId,
            @Param("serviceTypeId") String serviceTypeId,
            @Param("keyword") String keyword,
            Pageable pageable
    );


    List<Service> getServicesByIdIn(List<String> serviceIds);
}
