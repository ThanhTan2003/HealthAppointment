package com.programmingtechie.identity_service.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.identity_service.dto.request.Customer.CustomerRequest;
import com.programmingtechie.identity_service.dto.response.Customer.CustomerPatientResponse;
import com.programmingtechie.identity_service.dto.response.Customer.CustomerResponse;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.service.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/identity/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    final CustomerService customerService;

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

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or "
            + "hasRole('GiamDoc')") // Chp phep nguoi QuanTriVien moi co the su dung
    public PageResponse<CustomerResponse> getCustomers(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return customerService.getCustomers(page, size);
    }

    @PostMapping("/create")
    public CustomerResponse createCustomer(@RequestBody CustomerRequest request) {
        return customerService.createCustomer(request);
    }

    @PutMapping("/update/{id}")
    @PostAuthorize(
            "hasRole('QuanTriVienHeThong') or hasRole('GiamDoc') or returnObject.email == authentication.principal.claims['email']")
    public CustomerResponse updateCustomer(
            @PathVariable("id") String customerId, @RequestBody CustomerRequest request) {
        return customerService.updateCustomer(customerId, request);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public void deleteCustomer(@PathVariable("id") String customerId) {
        customerService.deleteCustomer(customerId);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('NguoiDung')")
    public CustomerResponse getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/email")
    @PostAuthorize(
            "hasRole('QuanTriVienHeThong') or hasRole('GiamDoc') or returnObject.email == authentication.principal.claims['email']")
    public CustomerResponse findCustomerByEmail(@RequestParam("email") String email) {
        return customerService.findCustomerByEmail(email);
    }

    @GetMapping("/phone-number")
    @PostAuthorize("" + "hasRole('QuanTriVienHeThong') or "
            + "hasRole('GiamDoc') or "
            + "returnObject.result.email == authentication.email")
    public CustomerResponse findCustomerByPhoneNumber(@RequestParam("phone") String phoneNumber) {
        return customerService.findCustomerByPhoneNumber(phoneNumber);
    }

    @GetMapping("/status")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public List<CustomerResponse> findCustomersByStatus(@RequestParam("status") String status) {
        return customerService.findCustomersByStatus(status);
    }

    @GetMapping("/get-info")
    @PostAuthorize("" + "hasRole('QuanTriVienHeThong') or "
            + "hasRole('GiamDoc') or "
            + "returnObject.email == authentication.principal.claims['email']")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getInfo() {
        return customerService.getMyInfo();
    }

    @GetMapping("/patient-details/{customerId}")
    @PreAuthorize(
            "hasRole('QuanTriVienHeThong') or hasRole('NguoiDung') or returnObject.email == authentication.principal.claims['email']")
    public CustomerPatientResponse getCustomerWithPatientDetails(
            @PathVariable("customerId") String customerId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return customerService.getCustomerPatientDetails(customerId, page, size);
    }
}
