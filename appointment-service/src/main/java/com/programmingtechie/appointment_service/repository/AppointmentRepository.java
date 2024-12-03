package com.programmingtechie.appointment_service.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.programmingtechie.appointment_service.model.Appointment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository
        extends JpaRepository<Appointment, String>, JpaSpecificationExecutor<Appointment> {

    @Query(value = "SELECT * FROM Appointment a WHERE " +
            "unaccent(LOWER(a.status)) LIKE unaccent(LOWER(CONCAT('%', :status, '%'))) AND " +
            "unaccent(LOWER(a.id)) LIKE unaccent(LOWER(CONCAT('%', :id, '%'))) " +
            "ORDER BY a.status ASC", nativeQuery = true)
    Page<Appointment> getAllAppointment(Pageable pageable, @Param("status") String status, @Param("id") String id);


    @Query("SELECT DISTINCT a.status FROM Appointment a")
    List<String> findDistinctStatuses();


    Page<Appointment> findByPatientsId(String id, Pageable pageable);

    // Phương thức đếm số lượng appointments theo serviceTimeFrameId và date
    long countByServiceTimeFrameIdAndDate(String serviceTimeFrameId, LocalDate date);

    @Query("SELECT a.orderNumber FROM Appointment a WHERE a.serviceTimeFrameId = :serviceTimeFrameId AND a.date = :date")
    List<Integer> findOrderNumbersByServiceTimeFrameIdAndDate(@Param("serviceTimeFrameId") String serviceTimeFrameId, @Param("date") LocalDate date);

    Page<Appointment> findByCustomerId(String customerId, Pageable pageable);

    Page<Appointment> findByCustomerIdAndPatientsId(String customerId, String patientId, Pageable pageable);
}

// JpaSpecificationExecutor là một interface trong Spring Data JPA,
// được sử dụng để hỗ trợ tìm kiếm linh hoạt (dynamic queries) dựa trên các điều kiện được định nghĩa bằng
// Specification