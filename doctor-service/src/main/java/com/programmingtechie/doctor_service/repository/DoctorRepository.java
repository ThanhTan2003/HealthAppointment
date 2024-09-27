package com.programmingtechie.doctor_service.repository;

import com.programmingtechie.doctor_service.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    @Query("SELECT d FROM Doctor d")
    Page<Doctor> getAllDoctor(Pageable pageable);

    Optional<Doctor> findByPhoneNumber(String phoneNumber);

    Optional<Doctor> findByGender(String gender);

    // Tìm danh sách bác sĩ theo Specialty với phân trang
    @Query("SELECT d FROM Doctor d JOIN d.specialties ds WHERE ds.specialty.id = :specialtyId")
    Page<Doctor> findDoctorsBySpecialty(@Param("specialtyId") String specialtyId, Pageable pageable);

    // Tìm danh sách bác sĩ theo Qualification với phân trang
    @Query("SELECT d FROM Doctor d JOIN d.doctorQualifications dq WHERE dq.qualification.abbreviation = :qualificationAbbreviation")
    Page<Doctor> findDoctorsByQualification(@Param("qualificationAbbreviation") String qualificationAbbreviation, Pageable pageable);
}
