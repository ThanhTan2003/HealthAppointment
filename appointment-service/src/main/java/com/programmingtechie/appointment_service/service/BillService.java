package com.programmingtechie.appointment_service.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.BillRequest;
import com.programmingtechie.appointment_service.dto.response.Payment.BillResponse;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.mapper.BillMapper;
import com.programmingtechie.appointment_service.model.Bill;
import com.programmingtechie.appointment_service.repository.BillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    private final BillMapper billMapper;

    public BillResponse createBill(BillRequest billRequest) {
        Bill bill = billMapper.toBillEntity(billRequest);
        bill = billRepository.save(bill);
        return billMapper.toBillResponse(bill);
    }

    public BillResponse getBillById(String id) {
        Bill bill = billRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));
        return billMapper.toBillResponse(bill);
    }

    public BillResponse updateBill(String id, BillRequest billRequest) {
        Bill bill = billRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));
        // billMapper.updateBillEntity(bill, billRequest);
        bill = billRepository.save(bill);
        return billMapper.toBillResponse(bill);
    }

    public void deleteBill(String id) {
        Bill bill = billRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + id));
        billRepository.delete(bill);
    }

    public PageResponse<BillResponse> getAllBills(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Bill> bills = billRepository.findAll(pageable);
        List<BillResponse> data = bills.stream().map(billMapper::toBillResponse).toList();
        return PageResponse.<BillResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(bills.getTotalPages())
                .totalElements(bills.getTotalElements())
                .data(data)
                .build();
    }
}
