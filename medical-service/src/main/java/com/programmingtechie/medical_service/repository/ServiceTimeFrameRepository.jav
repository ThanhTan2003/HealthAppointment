package com.programmingtechie.medical_service.repository;

import com.programmingtechie.medical_service.model.ServiceTimeFrame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTimeFrameRepository extends JpaRepository<ServiceTimeFrame, String> {
}