package com.programmingtechie.doctor_service.repository;

import com.programmingtechie.doctor_service.model.DoctorLeave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, String> {
}
