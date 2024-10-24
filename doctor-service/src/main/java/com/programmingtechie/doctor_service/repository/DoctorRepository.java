package com.programmingtechie.doctor_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.programmingtechie.doctor_service.model.Doctor;

@Repository
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
            value =
                    "SELECT * FROM public.doctor WHERE "
                            + "unaccent(LOWER(id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(full_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(gender)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(phone_number)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                            + "unaccent(LOWER(status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) "
                            + "ORDER BY unaccent(LOWER(split_part(full_name, ' ', array_length(string_to_array(full_name, ' '), 1)))) ASC",
            nativeQuery = true)
    Page<Doctor> searchDoctorsWithUnaccent(@Param("keyword") String keyword, Pageable pageable);

    @Query(
            value = "SELECT d.id AS doctor_id, d.full_name, d.gender, d.phone_number, d.status, "
                    + "s.id AS specialty_id, s.name AS specialty_name "
                    + // Đặt alias cho specialty id và name
                    "FROM public.doctor d "
                    + "LEFT JOIN public.doctor_specialty ds ON d.id = ds.doctor_id "
                    + "LEFT JOIN public.specialty s ON ds.specialty_id = s.id "
                    + "WHERE ("
                    + "   unaccent(LOWER(d.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "   unaccent(LOWER(d.full_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "   unaccent(LOWER(d.gender)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "   unaccent(LOWER(d.phone_number)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "   unaccent(LOWER(d.status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))"
                    + ")"
                    + "AND (:specialty IS NULL OR ds.specialty_id = :specialty) "
                    + "AND (:status IS NULL OR d.status = :status) ",
            countQuery = "SELECT COUNT(*) " + "FROM public.doctor d "
                    + "LEFT JOIN public.doctor_specialty ds ON d.id = ds.doctor_id "
                    + "LEFT JOIN public.specialty s ON ds.specialty_id = s.id "
                    + "WHERE ("
                    + "   unaccent(LOWER(d.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "   unaccent(LOWER(d.full_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "   unaccent(LOWER(d.gender)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "   unaccent(LOWER(d.phone_number)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "   unaccent(LOWER(d.status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))"
                    + ")"
                    + "AND (:specialty IS NULL OR ds.specialty_id = :specialty) "
                    + "AND (:status IS NULL OR d.status = :status) ",
            nativeQuery = true)
    Page<Object[]> searchDoctorsWithFilters(
            @Param("keyword") String keyword,
            @Param("specialty") String specialty,
            @Param("status") String status,
            Pageable pageable);
}
