package com.programmingtechie.appointment_service.enums;

import lombok.Getter;

@Getter
// trạng thái của giao dịch
public enum vnp_ResponseCode {
    SUCCESS("00", "Giao dịch thành công"),
    SUSPICIOUS_TRANSACTION(
            "07", "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường)"),
    NOT_REGISTERED(
            "09",
            "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng"),
    INCORRECT_OTP(
            "10", "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần"),
    EXPIRED_PAYMENT(
            "11",
            "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch"),
    ACCOUNT_BLOCKED("12", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa"),
    INCORRECT_PASSWORD(
            "13",
            "Giao dịch không thành công do: Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch"),
    CUSTOMER_CANCELED("24", "Giao dịch không thành công do: Khách hàng hủy giao dịch"),
    INSUFFICIENT_FUNDS(
            "51", "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch"),
    EXCEEDED_DAILY_LIMIT(
            "65", "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày"),
    BANK_MAINTENANCE("75", "Ngân hàng thanh toán đang bảo trì"),
    EXCEEDED_OTP_ATTEMPTS(
            "79",
            "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch"),
    OTHER_ERROR("99", "Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê)");

    private final String code;
    private final String description;

    // Constructor
    vnp_ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // Phương thức lấy mã lỗi từ code
    public static vnp_ResponseCode fromCode(String code) {
        for (vnp_ResponseCode responseCode : vnp_ResponseCode.values()) {
            if (responseCode.getCode().equals(code)) {
                return responseCode;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy mã lỗi với giá trị: " + code);
    }
}
