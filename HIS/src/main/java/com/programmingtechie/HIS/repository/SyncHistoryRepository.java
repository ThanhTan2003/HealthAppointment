package com.programmingtechie.HIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.HIS.model.SyncHistory;

public interface SyncHistoryRepository extends JpaRepository<SyncHistory, String> {}
