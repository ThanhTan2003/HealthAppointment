package com.programmingtechie.appointment_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "payment",
        indexes = {
                @Index(name = "idx_id", columnList = "id"),
                @Index(name = "idx_appointment", columnList = "appointment_id")
        })
public class Payment {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

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

    @Column(name = "transaction_no")
    private String transactionNo;

    @Column(name = "response_code")
    private String responseCode;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @ManyToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;

}
