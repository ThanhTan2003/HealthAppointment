package com.programmingtechie.HIS.repository;

import com.programmingtechie.HIS.model.SyncHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncHistoryRepository extends JpaRepository<SyncHistory, String> {
}
