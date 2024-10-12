package com.programmingtechie.medical_service.repository;

import com.programmingtechie.medical_service.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceRepository extends JpaRepository<Service, String> {

    @Query("SELECT s FROM Service s")
    Page<Service> getAllService(Pageable pageable);

    // Tìm kiếm dịch vụ theo từ khoá với phân trang
    @Query(value = "SELECT * FROM service s WHERE " +
            "unaccent(LOWER(s.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(s.status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    Page<Service> searchServices(@Param("keyword") String keyword, Pageable pageable);

    // Tìm kiếm dịch vụ theo ServiceTypeId với phân trang
    @Query("SELECT s FROM Service s WHERE s.serviceType.id = :serviceTypeId")
    Page<Service> findByServiceTypeId(@Param("serviceTypeId") String serviceTypeId, Pageable pageable);
    // Lưu ý: Trong HQL, sẽ sử dụng tên thuộc tính serviceType chứ không phải tên cột service_type_id

    // Tìm kiếm dịch vụ theo SpecialtyId với phân trang
    @Query("SELECT s FROM Service s WHERE s.specialtyId = :specialtyId")
    Page<Service> findBySpecialtyId(@Param("specialtyId") String specialtyId, Pageable pageable);
}
