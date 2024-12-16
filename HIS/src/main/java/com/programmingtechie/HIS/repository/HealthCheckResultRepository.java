package com.programmingtechie.HIS.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.programmingtechie.HIS.model.HealthCheckResult;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, String> {
    @Query(
            value = "SELECT * FROM health_check_result a WHERE " + "a.last_updated BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Page<HealthCheckResult> findByLastUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
