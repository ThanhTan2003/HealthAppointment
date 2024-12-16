package com.programmingtechie.appointment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.appointment_service.model.HealthCheckResult;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, String> {}
