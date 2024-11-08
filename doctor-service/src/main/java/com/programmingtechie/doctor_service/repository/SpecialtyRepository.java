package com.programmingtechie.doctor_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.doctor_service.model.Specialty;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<Specialty, String> {

    @Query(
            value = "SELECT * FROM public.specialty WHERE "
                    + "unaccent(LOWER(name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    Page<Specialty> searchSpecialties(@Param("keyword") String keyword, Pageable pageable);

    List<Specialty> findByIdIn(List<String> specialtyIds);
}
