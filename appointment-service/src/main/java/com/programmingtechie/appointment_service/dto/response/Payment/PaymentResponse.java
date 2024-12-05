package com.programmingtechie.appointment_service.dto.response.Payment;

import com.programmingtechie.appointment_service.dto.request.PaymentRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private PaymentRequest paymentRequest;

    private String ipAddress;

    private String VNPayURL;
}
