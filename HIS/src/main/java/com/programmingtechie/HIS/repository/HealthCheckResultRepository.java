package com.programmingtechie.HIS.repository;

import com.programmingtechie.HIS.model.HealthCheckResult;
import com.programmingtechie.HIS.model.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, String> {
}
