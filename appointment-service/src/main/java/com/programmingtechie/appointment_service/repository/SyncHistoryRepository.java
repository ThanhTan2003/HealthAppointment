package com.programmingtechie.appointment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.appointment_service.model.SyncHistory;

public interface SyncHistoryRepository extends JpaRepository<SyncHistory, String> {}
