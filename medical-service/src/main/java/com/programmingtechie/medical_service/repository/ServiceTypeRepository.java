package com.programmingtechie.medical_service.repository;

import com.programmingtechie.medical_service.model.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, String> {
    @Query("SELECT st FROM ServiceType st ORDER BY st.id ASC")
    Page<ServiceType> getAllServiceTypes(Pageable pageable);

    @Query(value = "SELECT * FROM service_type st WHERE " +
            "unaccent(LOWER(st.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    Page<ServiceType> searchServiceTypes(@Param("keyword") String keyword, Pageable pageable);
}
