package com.programmingtechie.appointment_service.model;

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
        indexes = {@Index(name = "idx_id", columnList = "id")})
public class Bill {
    @Id
    @Column(nullable = false, length = 36)
    private String id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

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

    @Column(name = "payment_method", columnDefinition = "TEXT")
    private String paymentMethod;

    @Column(name = "status_checkout", columnDefinition = "TEXT")
    private String statusCheckout;

    @OneToOne(mappedBy = "bill")
    private Appointment appointment;
}
