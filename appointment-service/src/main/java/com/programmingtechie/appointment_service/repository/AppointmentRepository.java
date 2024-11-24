package com.programmingtechie.appointment_service.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.programmingtechie.appointment_service.model.Appointment;

public interface AppointmentRepository
        extends JpaRepository<Appointment, String>, JpaSpecificationExecutor<Appointment> {
    List<Appointment> findByPatientsId(String id);

    List<Appointment> findByDate(LocalDate date);

    List<Appointment> findByServiceTimeFrameIdAndDate(String serviceTimeFrameId, LocalDate date);
}

// JpaSpecificationExecutor là một interface trong Spring Data JPA,
// được sử dụng để hỗ trợ tìm kiếm linh hoạt (dynamic queries) dựa trên các điều kiện được định nghĩa bằng
// Specification
