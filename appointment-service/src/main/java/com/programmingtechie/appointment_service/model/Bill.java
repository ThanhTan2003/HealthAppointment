package com.programmingtechie.appointment_service.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "bill",
        indexes = {
            @Index(name = "idx_id", columnList = "id"),
            @Index(name = "idx_appointment", columnList = "appointment_id")
        })
public class Bill {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "service_time_frame_id")
    private String serviceTimeFrameId;

    @Column(name = "patients_id")
    private String patientsId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "order_number")
    private Integer orderNumber;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "expiry_date_time")
    private LocalDateTime expiryDateTime;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "surcharge")
    private Double surcharge;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "payment_amount")
    private Double paymentAmount;

    @Column(name = "status")
    private Integer status;

    @Column(name = "appointment_id")
    private String appointmentId; // ID lịch hẹn liên quan

    @Column(name = "note")
    private String note; // Ghi chú của hóa đơn

    @Column(name = "transaction_no")
    private String TransactionNo; // ID yêu cầu thanh toán (nếu có)

    @Column(name = "response_code")
    private String responseCode; // Mã phản hồi từ VNPAY

    @Column(name = "transaction_status")
    private String transactionStatus; // Trạng thái giao dịch của VNPAY
}
