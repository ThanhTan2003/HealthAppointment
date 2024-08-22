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

    Optional<Doctor> findFirstByPhoneNumberOrEmailOrIdentificationCode(String phoneNumber, String email, String identificationCode);

    Optional<Doctor> findByPhoneNumber(String phoneNumber);

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByGender(String gender);
}
