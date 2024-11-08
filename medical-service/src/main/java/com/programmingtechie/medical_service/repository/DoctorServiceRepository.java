package com.programmingtechie.medical_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.medical_service.model.DoctorService;

public interface DoctorServiceRepository extends JpaRepository<DoctorService, String> {
    Page<DoctorService> findAll(Pageable pageable);

    boolean existsByDoctorIdAndServiceId(String doctorId, String serviceId);

    Page<DoctorService> findByDoctorId(String doctorId, Pageable pageable);
}
