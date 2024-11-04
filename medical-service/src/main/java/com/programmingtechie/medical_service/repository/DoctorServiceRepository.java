package com.programmingtechie.medical_service.repository;

import com.programmingtechie.medical_service.model.DoctorService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorServiceRepository extends JpaRepository<DoctorService, String> {
}
