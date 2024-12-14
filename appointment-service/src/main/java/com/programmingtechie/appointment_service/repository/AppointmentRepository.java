package com.programmingtechie.appointment_service.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.appointment_service.model.Appointment;

public interface AppointmentRepository
        extends JpaRepository<Appointment, String>, JpaSpecificationExecutor<Appointment> {

    @Query(
            value = "SELECT * FROM Appointment a WHERE "
                    + "unaccent(LOWER(a.status)) LIKE unaccent(LOWER(CONCAT('%', :status, '%'))) AND "
                    + "unaccent(LOWER(a.id)) LIKE unaccent(LOWER(CONCAT('%', :id, '%'))) "
                    + "ORDER BY a.status ASC, a.date DESC",
            nativeQuery = true)
    Page<Appointment> getAllAppointment(Pageable pageable, @Param("status") String status, @Param("id") String id);

    @Query("SELECT DISTINCT a.status FROM Appointment a")
    List<String> findDistinctStatuses();

    Page<Appointment> findByPatientsId(String id, Pageable pageable);

    // Phương thức đếm số lượng appointments theo serviceTimeFrameId và date
    long countByServiceTimeFrameIdAndDate(String serviceTimeFrameId, LocalDate date);

    @Query(
            "SELECT a.orderNumber FROM Appointment a WHERE a.serviceTimeFrameId = :serviceTimeFrameId AND a.date = :date")
    List<Integer> findOrderNumbersByServiceTimeFrameIdAndDate(
            @Param("serviceTimeFrameId") String serviceTimeFrameId, @Param("date") LocalDate date);

    Page<Appointment> findByCustomerId(String customerId, Pageable pageable);

    Page<Appointment> findByCustomerIdAndPatientsId(String customerId, String patientId, Pageable pageable);

    @Query(
            "SELECT COUNT(a) > 0 FROM Appointment a WHERE a.patientsId = :patientsId AND a.serviceTimeFrameId = :serviceTimeFrameId AND a.date = :date")
    boolean existsByPatientsIdAndServiceTimeFrameIdAndDate(
            @Param("patientsId") String patientsId,
            @Param("serviceTimeFrameId") String serviceTimeFrameId,
            @Param("date") LocalDate date);

    @Query(
            "SELECT a FROM Appointment a WHERE a.patientsId IN :patientIds AND a.serviceTimeFrameId = :serviceTimeFrameId AND a.date = :date")
    List<Appointment> findAllByPatientIdInAndServiceTimeFrameIdAndDate(
            @Param("patientIds") List<String> patientIds,
            @Param("serviceTimeFrameId") String serviceTimeFrameId,
            @Param("date") LocalDate date);

    Optional<Appointment> findByPaymentId(String id);

    @Query(value = "SELECT * FROM appointment a WHERE " +
            "unaccent(LOWER(a.status)) LIKE unaccent(LOWER('Đã xác nhận')) " +
            "AND a.last_updated BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Page<Appointment> findByStatusAndLastUpdatedBetween(
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}

// JpaSpecificationExecutor là một interface trong Spring Data JPA,
// được sử dụng để hỗ trợ tìm kiếm linh hoạt (dynamic queries) dựa trên các điều
// kiện được định nghĩa bằng
// Specification
