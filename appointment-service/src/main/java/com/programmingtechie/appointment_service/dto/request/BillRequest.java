package com.programmingtechie.appointment_service.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest {
    private LocalDateTime dateTime;

    private Double unitPrice;

    private Double surcharge;

    private Double totalAmount;

    private Double discount;

    private Double paymentAmount;

    private String paymentMethod;

    private String statusCheckout;

    private String appointmentId;
}
