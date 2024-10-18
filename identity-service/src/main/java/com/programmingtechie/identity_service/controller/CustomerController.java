package com.programmingtechie.identity_service.controller;

import com.programmingtechie.identity_service.dto.request.Customer.CustomerRequest;
import com.programmingtechie.identity_service.dto.response.Customer.CustomerResponse;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.dto.response.UserResponse;
import com.programmingtechie.identity_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    @PreAuthorize("hasRole('QuanTriVien')") // Chp phep nguoi QuanTriVien moi co the su dung
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
    @PostAuthorize("hasRole('QuanTriVien') or returnObject.email == authentication.principal.claims['email']")
    public CustomerResponse updateCustomer(
            @PathVariable("id") String customerId, @RequestBody CustomerRequest request) {
        return customerService.updateCustomer(customerId, request);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('QuanTriVien')")
    public void deleteCustomer(@PathVariable("id") String customerId) {
        customerService.deleteCustomer(customerId);
    }

    @GetMapping("/email")
    @PostAuthorize("hasRole('QuanTriVien') or returnObject.email == authentication.principal.claims['email']")
    public CustomerResponse findCustomerByEmail(@RequestParam("email") String email) {
        return customerService.findCustomerByEmail(email);
    }

    @GetMapping("/phone-number")
    @PostAuthorize("hasRole('QuanTriVien') or returnObject.result.email == authentication.email")
    public CustomerResponse findCustomerByPhoneNumber(@RequestParam("phone") String phoneNumber) {
        return customerService.findCustomerByPhoneNumber(phoneNumber);
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('QuanTriVien')")
    public List<CustomerResponse> findCustomersByStatus(@RequestParam("status") String status) {
        return customerService.findCustomersByStatus(status);
    }

    // Lay thong tin dang nhap
    @GetMapping("/get-info")
    @PostAuthorize("hasRole('QuanTriVien') or returnObject.email == authentication.principal.claims['email']")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getInfo() {
        return customerService.getMyInfo();
    }
}