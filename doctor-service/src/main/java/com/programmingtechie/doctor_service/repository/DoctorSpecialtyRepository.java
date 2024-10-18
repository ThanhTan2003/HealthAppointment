package com.programmingtechie.doctor_service.repository;

import com.programmingtechie.doctor_service.model.DoctorSpecialty;
import com.programmingtechie.doctor_service.model.DoctorSpecialtyId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorSpecialtyRepository extends JpaRepository<DoctorSpecialty,DoctorSpecialtyId> {
}
