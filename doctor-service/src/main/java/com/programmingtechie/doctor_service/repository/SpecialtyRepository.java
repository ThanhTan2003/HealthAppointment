package com.programmingtechie.doctor_service.repository;

import com.programmingtechie.doctor_service.model.Doctor;
import com.programmingtechie.doctor_service.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyRepository extends JpaRepository<Specialty, String> {
}
