package com.programmingtechie.appointment_service.repository;


import com.programmingtechie.appointment_service.model.SyncHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncHistoryRepository extends JpaRepository<SyncHistory, String> {
}
