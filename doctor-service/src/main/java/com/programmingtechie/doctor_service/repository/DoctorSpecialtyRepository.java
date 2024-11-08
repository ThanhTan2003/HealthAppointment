package com.programmingtechie.doctor_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.doctor_service.model.DoctorSpecialty;
import com.programmingtechie.doctor_service.model.DoctorSpecialtyId;

public interface DoctorSpecialtyRepository extends JpaRepository<DoctorSpecialty, DoctorSpecialtyId> {}
