package com.programmingtechie.doctor_service.repository;

import com.programmingtechie.doctor_service.model.DoctorQualification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorQualificationRepository extends JpaRepository<DoctorQualification, String> {
}
