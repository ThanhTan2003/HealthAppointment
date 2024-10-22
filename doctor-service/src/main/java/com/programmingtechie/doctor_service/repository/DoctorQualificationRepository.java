package com.programmingtechie.doctor_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.doctor_service.model.DoctorQualification;
import com.programmingtechie.doctor_service.model.DoctorQualificationId;

@Repository
public interface DoctorQualificationRepository extends JpaRepository<DoctorQualification, DoctorQualificationId> {
}
