package com.programmingtechie.medical_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.programmingtechie.medical_service.model.Holiday;
import com.programmingtechie.medical_service.model.HolidayId;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, HolidayId> {
    @Query(
            value = "SELECT * FROM holiday h WHERE "
                    + "unaccent(LOWER(h.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    List<Holiday> searchHolidayDays(@Param("keyword") String keyword);

    Optional<Holiday> findByDayAndMonth(Integer day, Integer month);
}
