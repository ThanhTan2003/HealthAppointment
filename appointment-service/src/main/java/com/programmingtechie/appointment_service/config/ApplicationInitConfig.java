package com.programmingtechie.appointment_service.config;

import java.time.LocalDateTime;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.programmingtechie.appointment_service.enums.Sync_History;
import com.programmingtechie.appointment_service.model.SyncHistory;
import com.programmingtechie.appointment_service.repository.SyncHistoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    final SyncHistoryRepository syncHistoryRepository;

    @Bean
    ApplicationRunner initializeSyncHistory() {
        return args -> {
            for (Sync_History syncHistory : Sync_History.values()) {
                if (!syncHistoryRepository.existsById(syncHistory.getName())) {
                    SyncHistory syncHistoryEntity = SyncHistory.builder()
                            .id(syncHistory.getName())
                            .dateTime(LocalDateTime.of(1990, 1, 1, 0, 0, 0, 0)) // DateTime năm 1990
                            .build();
                    syncHistoryRepository.save(syncHistoryEntity);
                    log.info("Đã lưu SyncHistory với id: {}", syncHistory.getName());
                }
            }
        };
    }
}
