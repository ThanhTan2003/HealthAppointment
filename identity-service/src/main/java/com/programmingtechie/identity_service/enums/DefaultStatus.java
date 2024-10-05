package com.programmingtechie.identity_service.enums;

import lombok.Getter;

@Getter
public enum DefaultStatus {
    DANG_HOAT_DONG("DangHoatDong", "Đang hoạt động"),
    TAM_KHOA("TamKhoa", "Tạm khóa"),
    NGUNG_SU_DUNG("NgungSuDung", "Ngừng sử dụng");

    private final String id;
    private final String name;

    DefaultStatus(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
