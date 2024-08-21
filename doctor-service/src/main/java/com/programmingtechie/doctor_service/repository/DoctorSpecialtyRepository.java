package com.programmingtechie.doctor_service.repository;

import com.programmingtechie.doctor_service.model.DoctorSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorSpecialtyRepository extends JpaRepository<DoctorSpecialty, String> {
}
