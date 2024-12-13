package com.programmingtechie.HIS.enums;

import lombok.Getter;

@Getter
public enum Sync_History {
    DOCTOR("doctor"),
    DOCTOR_QUALIFICATION( "doctor_qualification"),
    SPECIALTY("specialty"),
    DOCTOR_SPECIALTY("doctor_specialty"),
    SERVICE("service"),
    APPOINTMENT("appointment");

    private final String name;

    Sync_History(String name) {
        this.name = name;
    }
}
