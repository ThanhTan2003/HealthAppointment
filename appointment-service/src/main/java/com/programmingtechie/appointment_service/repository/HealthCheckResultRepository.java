package com.programmingtechie.appointment_service.repository;

import com.programmingtechie.appointment_service.model.HealthCheckResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, String> {
}
