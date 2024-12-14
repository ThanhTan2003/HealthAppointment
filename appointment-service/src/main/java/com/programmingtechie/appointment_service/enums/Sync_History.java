package com.programmingtechie.appointment_service.enums;

import lombok.Getter;

@Getter
public enum Sync_History {
    HEALTH_CHECK_RESULTS("health_check_result"),
    HEALTH_CHECK_RESULTS_DELETED("health_check_result_deleted");

    private final String name;

    Sync_History(String name) {
        this.name = name;
    }
}
