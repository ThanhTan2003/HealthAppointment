package com.programmingtechie.appointment_service.constant;

public class VNPayIPNResponse {
    public static final String AMOUNT = "vnp_Amount"; // Số tiền giao dịch
    public static final String BANK_CODE = "vnp_BankCode"; // Mã ngân hàng
    public static final String BANK_TRAN_NO = "vnp_BankTranNo"; // Mã giao dịch tại ngân hàng
    public static final String CARD_TYPE = "vnp_CardType"; // Loại thẻ
    public static final String ORDER_INFO = "vnp_OrderInfo"; // Thông tin đơn hàng
    public static final String PAY_DATE = "vnp_PayDate"; // Thời gian thanh toán
    public static final String RESPONSE_CODE = "vnp_ResponseCode"; // Mã phản hồi giao dịch
    public static final String TMN_CODE = "vnp_TmnCode"; // Mã terminal của merchant
    public static final String TRANSACTION_NO = "vnp_TransactionNo"; // Mã giao dịch tại VNPAY
    public static final String TXN_REF = "vnp_TxnRef"; // Mã tham chiếu giao dịch
    public static final String SECURE_HASH_TYPE = "vnp_SecureHashType"; // Loại mã hóa chữ ký
    public static final String SECURE_HASH = "vnp_SecureHash"; // Chữ ký bảo mật
}
