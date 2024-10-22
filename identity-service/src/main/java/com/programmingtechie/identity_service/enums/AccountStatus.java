package com.programmingtechie.identity_service.enums;

import lombok.*;

@Getter
public enum AccountStatus {
    DANG_HOAT_DONG("DangHoatDong", "Đang hoạt động"),
    TAM_KHOA("TamKhoa", "Tạm khóa"),
    NGUNG_SU_DUNG("NgungSuDung", "Ngừng sử dụng");

    private final String id;
    private final String name;

    AccountStatus(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
