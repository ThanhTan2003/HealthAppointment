package com.programmingtechie.appointment_service.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.appointment_service.model.Bill;

public interface BillRepository extends JpaRepository<Bill, String> {
    @Query(
            "SELECT COUNT(a) > 0 FROM Bill a WHERE a.patientsId = :patientsId AND a.serviceTimeFrameId = :serviceTimeFrameId AND a.date = :date")
    boolean existsByPatientsIdAndServiceTimeFrameIdAndDate(
            @Param("patientsId") String patientsId,
            @Param("serviceTimeFrameId") String serviceTimeFrameId,
            @Param("date") LocalDate date);
}
