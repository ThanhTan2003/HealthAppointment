package com.programmingtechie.doctor_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.doctor_service.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    @Query("SELECT d FROM Doctor d")
    Page<Doctor> getAllDoctor(Pageable pageable);

    Optional<Doctor> findByPhoneNumber(String phoneNumber);

    Optional<Doctor> findByGender(String gender);

    // Tìm danh sách bác sĩ theo Specialty với phân trang
    @Query("SELECT d FROM Doctor d JOIN d.specialties ds WHERE ds.specialty.id = :specialtyId")
    Page<Doctor> findDoctorsBySpecialty(@Param("specialtyId") String specialtyId, Pageable pageable);

    // Tìm danh sách bác sĩ theo Qualification với phân trang
    @Query(
            "SELECT d FROM Doctor d JOIN d.doctorQualifications dq WHERE dq.qualification.abbreviation = :qualificationAbbreviation")
    Page<Doctor> findDoctorsByQualification(
            @Param("qualificationAbbreviation") String qualificationAbbreviation, Pageable pageable);

    // Tìm kiếm bác sĩ thông thường
    @Query("SELECT d FROM Doctor d WHERE " + "LOWER(d.id) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(d.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(d.gender) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(d.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(d.status) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Doctor> searchDoctors(@Param("keyword") String keyword, Pageable pageable);

    // Tìm kiếm bác sĩ sử dụng extension unaccent
    @Query(
            value = "SELECT * FROM public.doctor WHERE "
                    + "unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(full_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(gender)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(phone_number)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    Page<Doctor> searchDoctorsWithUnaccent(@Param("keyword") String keyword, Pageable pageable);
}
