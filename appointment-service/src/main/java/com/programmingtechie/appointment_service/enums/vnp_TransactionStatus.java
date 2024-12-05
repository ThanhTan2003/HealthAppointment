package com.programmingtechie.appointment_service.enums;

import lombok.Getter;

@Getter
// phản hồi từ hệ thống VNPAY
public enum vnp_TransactionStatus {
    SUCCESS("00", "Giao dịch thành công"),
    PENDING("01", "Giao dịch chưa hoàn tất"),
    ERROR("02", "Giao dịch bị lỗi"),
    REVERSE("04", "Giao dịch đảo (Khách hàng đã bị trừ tiền tại Ngân hàng nhưng GD chưa thành công ở VNPAY)"),
    PROCESSING_REFUND("05", "VNPAY đang xử lý giao dịch này (GD hoàn tiền)"),
    REFUND_REQUESTED("06", "VNPAY đã gửi yêu cầu hoàn tiền sang Ngân hàng (GD hoàn tiền)"),
    SUSPICIOUS_FRAUD("07", "Giao dịch bị nghi ngờ gian lận"),
    REFUND_DECLINED("09", "GD Hoàn trả bị từ chối");

    private final String code;
    private final String description;

    // Constructor
    vnp_TransactionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // Phương thức lấy trạng thái từ mã trạng thái
    public static vnp_TransactionStatus fromCode(String code) {
        for (vnp_TransactionStatus status : vnp_TransactionStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái giao dịch với mã: " + code);
    }

}


