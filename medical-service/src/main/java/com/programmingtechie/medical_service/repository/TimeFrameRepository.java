package com.programmingtechie.medical_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.medical_service.model.TimeFrame;

public interface TimeFrameRepository extends JpaRepository<TimeFrame, String> {
    List<TimeFrame> findAllByOrderByStartTimeAsc();
}
