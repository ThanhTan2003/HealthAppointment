package com.programmingtechie.HIS.repository;

import com.programmingtechie.HIS.model.HealthCheckResult;
import com.programmingtechie.HIS.model.HealthCheckResultsDeleted;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface HealthCheckResultsDeletedRepository extends JpaRepository<HealthCheckResultsDeleted, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM HealthCheckResultsDeleted h WHERE h.lastUpdated <= :endTime")
    void deleteByLastUpdatedBefore(@Param("endTime") LocalDateTime endTime);

    @Query(value = "SELECT * FROM health_check_result_deleted a WHERE " +
            "a.last_updated < :endDate",
            nativeQuery = true)
    Page<HealthCheckResultsDeleted> findByLastUpdatedBefore(
            LocalDateTime endDate, Pageable pageable);
}
