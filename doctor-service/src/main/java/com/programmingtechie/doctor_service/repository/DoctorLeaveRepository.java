package com.programmingtechie.doctor_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.doctor_service.model.DoctorLeave;

@Repository
public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, String> {
}
