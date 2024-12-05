package com.programmingtechie.appointment_service.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    NOT_STARTED(0, "Chưa thanh toán"),
    PAYMENT_PENDING(1, "Đang chờ thanh toán"),
    PAYMENT_PROCESSING(2, "Đang xử lý thanh toán"),
    PAYMENT_COMPLETED(3, "Thanh toán thành công"),
    PAYMENT_FAILED(4, "Thanh toán thất bại"),
    REFUNDED(5, "Đã hoàn tiền"),
    CANCELLED(6, "Đã hủy thanh toán");

    private final int value; // Giá trị trạng thái
    private final String name; // Tên trạng thái

    // Constructor
    PaymentStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    // Phương thức tìm trạng thái từ giá trị
    public static PaymentStatus fromValue(int value) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái với giá trị: " + value);
    }
}
