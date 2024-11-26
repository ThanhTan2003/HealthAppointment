package com.programmingtechie.medical_service.repository;

import com.programmingtechie.medical_service.model.TimeFrame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeFrameRepository extends JpaRepository<TimeFrame, String> {
    List<TimeFrame> findAllByOrderByStartTimeAsc();
}
