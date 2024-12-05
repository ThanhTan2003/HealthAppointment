package com.programmingtechie.appointment_service.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.request.BillRequest;
import com.programmingtechie.appointment_service.dto.response.Payment.BillResponse;
import com.programmingtechie.appointment_service.model.Bill;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BillMapper {
    public Bill toBillEntity(BillRequest billRequest) {
        return Bill.builder()
                .dateTime(LocalDateTime.now())
                .unitPrice(billRequest.getUnitPrice())
                .surcharge(billRequest.getSurcharge())
                .totalAmount(billRequest.getTotalAmount())
                .discount(billRequest.getDiscount())
                .paymentAmount(billRequest.getPaymentAmount())
                .build();
    }

    public BillResponse toBillResponse(Bill bill) {
        return BillResponse.builder()
                .id(bill.getId())
                .dateTime(bill.getDateTime())
                .unitPrice(bill.getUnitPrice())
                .surcharge(bill.getSurcharge())
                .totalAmount(bill.getTotalAmount())
                .discount(bill.getDiscount())
                .paymentAmount(bill.getPaymentAmount())
                .build();
    }

    public void updateBillEntity(Bill bill, BillRequest billRequest) {
        bill.setUnitPrice(billRequest.getUnitPrice());
        bill.setSurcharge(billRequest.getSurcharge());
        bill.setTotalAmount(billRequest.getTotalAmount());
        bill.setDiscount(billRequest.getDiscount());
        bill.setPaymentAmount(billRequest.getPaymentAmount());
    }
}
