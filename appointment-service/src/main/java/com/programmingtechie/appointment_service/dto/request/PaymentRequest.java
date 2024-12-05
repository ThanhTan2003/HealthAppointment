package com.programmingtechie.appointment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String customerId;

    private Integer orderNumber;

    private Double unitPrice;

    private Double surcharge;

    private Double totalAmount;

    private Double discount;

    private Double paymentAmount;

    private AppointmentRequest appointmentRequest;
}
