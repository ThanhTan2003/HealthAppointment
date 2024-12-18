package com.programmingtechie.appointment_service.dto.request;

import java.time.LocalDate;
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
    private LocalDate date;

    private String serviceTimeFrameId;

    private String patientsId;

    private String customerId;

    private Integer orderNumber;

    private Double unitPrice;

    private Double surcharge;

    private Double totalAmount;

    private Double discount;

    private Double paymentAmount;

    private Integer status;

    private LocalDateTime dateTime;
}
