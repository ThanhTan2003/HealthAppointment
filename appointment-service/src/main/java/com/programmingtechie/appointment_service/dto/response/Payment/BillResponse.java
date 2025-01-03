package com.programmingtechie.appointment_service.dto.response.Payment;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    private String id;

    private LocalDateTime dateTime;

    private Double unitPrice;

    private Double surcharge;

    private Double totalAmount;

    private Double discount;

    private Double paymentAmount;

    private String paymentMethod;

    private String statusCheckout;

    private String appointmentId;

    private String VNPayURL;
}
