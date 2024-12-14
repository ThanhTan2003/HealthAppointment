package com.programmingtechie.HIS.repository;

import com.programmingtechie.HIS.model.HealthCheckResult;
import com.programmingtechie.HIS.model.Qualification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, String> {
    @Query(value = "SELECT * FROM health_check_result a WHERE " +
            "a.last_updated BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Page<HealthCheckResult> findByLastUpdatedBetween(
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
