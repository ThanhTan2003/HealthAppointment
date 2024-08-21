package com.programmingtechie.doctor_service.repository;

import com.programmingtechie.doctor_service.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualificationRepository extends JpaRepository<Qualification, String> {
}
