package com.programmingtechie.appointment_service.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.appointment_service.dto.request.BillRequest;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.Payment.BillResponse;
import com.programmingtechie.appointment_service.service.BillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment/bill")
@RequiredArgsConstructor
public class BillController {
    final BillService billService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        // Tạo body của response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false)); // đường dẫn của request

        // Trả về response với mã 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping
    public ResponseEntity<BillResponse> createBill(@RequestBody BillRequest billRequest) {
        return ResponseEntity.ok(billService.createBill(billRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponse> getBillById(@PathVariable String id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillResponse> updateBill(@PathVariable String id, @RequestBody BillRequest billRequest) {
        return ResponseEntity.ok(billService.updateBill(id, billRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable String id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<BillResponse>> getAllBills(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(billService.getAllBills(page, size));
    }
}
