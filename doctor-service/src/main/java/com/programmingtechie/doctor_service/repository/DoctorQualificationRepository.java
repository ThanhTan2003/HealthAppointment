package com.programmingtechie.doctor_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.doctor_service.model.DoctorQualification;
import com.programmingtechie.doctor_service.model.DoctorQualificationId;

public interface DoctorQualificationRepository extends JpaRepository<DoctorQualification, DoctorQualificationId> {}
