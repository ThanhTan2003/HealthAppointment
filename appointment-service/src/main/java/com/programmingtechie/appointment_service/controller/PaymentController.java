package com.programmingtechie.appointment_service.controller;

import com.programmingtechie.appointment_service.dto.response.Payment.IpnResponse;
import com.programmingtechie.appointment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/api/v1/appointment/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/public/vnpay_ipn")
    void processIpn(@RequestParam Map<String, String> params) {
        log.info("Nhan yeu cau tu VNPay...");
        paymentService.processIpn(params);
    }
}
