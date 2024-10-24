package com.programmingtechie.identity_service.controller;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.identity_service.dto.request.Customer.CustomerRequest;
import com.programmingtechie.identity_service.dto.response.Customer.CustomerResponse;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.service.CustomerServiceV1;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/identity/customer")
@RequiredArgsConstructor
public class CustomerController {
    final CustomerServiceV1 customerServiceV1;

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

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createCustomer(@RequestBody CustomerRequest customerRequest) {
        customerServiceV1.createCustomer(customerRequest);
        return "Tạo tài khoản thành công!";
    }

    @PutMapping("/update/{id}")
    @PostAuthorize("hasRole('QuanTriVien') or returnObject.email == authentication.principal.claims['email']")
    public String updateCustomer(@PathVariable String id, @RequestBody CustomerRequest customerRequest) {
        customerServiceV1.updateCustomer(id, customerRequest);
        return "Cập nhật thông tin thành công";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteCustomer(@PathVariable String id) {
        customerServiceV1.deleteCustomer(id);
        return "Xóa thông tin khách hàng thành công!";
    }

    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('QuanTriVien')")
    public PageResponse<CustomerResponse> getCustomers(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return customerServiceV1.getCustomers(page, size);
    }

    @GetMapping("/full-name")
    @PostAuthorize("hasRole('QuanTriVien') or returnObject.email == authentication.principal.claims['email']")
    public List<CustomerResponse> getCustomerByFullName(@RequestParam("name") String name) {
        return customerServiceV1.getCustomerByFullName(name);
    }

    @GetMapping("/email")
    @PostAuthorize("hasRole('QuanTriVien') or returnObject.email == authentication.principal.claims['email']")
    public CustomerResponse getCustomerByEmail(@RequestParam("email") String email) {
        return customerServiceV1.getCustomerByEmail(email);
    }

    @GetMapping("/phone-number")
    @PostAuthorize("hasRole('QuanTriVien') or returnObject.result.email == authentication.email")
    public CustomerResponse getCustomerByPhone(@RequestParam("phone") String phoneNumber) {
        return customerServiceV1.getCustomerByPhone(phoneNumber);
    }

    @GetMapping("/info")
    @PostAuthorize("hasRole('QuanTriVien') or returnObject.email == authentication.principal.claims['email']")
    public CustomerResponse getCustomerInfo(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone) {

        if (email != null) {
            return customerServiceV1.getCustomerByEmail(email);
        } else if (phone != null) {
            return customerServiceV1.getCustomerByPhone(phone);
        } else {
            throw new IllegalArgumentException("At least one query parameter (name, email, phone) must be provided");
        }
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('QuanTriVien')")
    public List<CustomerResponse> findCustomersByStatus(@RequestParam("status") String status) {
        return customerServiceV1.findCustomersByStatus(status);
    }

    @GetMapping("/get-info")
    @PostAuthorize("returnObject.email == authentication.principal.claims['email']")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse getInfo() {
        return customerServiceV1.getInfo();
    }

    // Tìm khách hàng phương thức GET có phân trang
    @GetMapping("/search")
    public ResponseEntity<PageResponse<CustomerResponse>> searchCustomers(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(customerServiceV1.searchCustomers(keyword, page, size));
    }

    // Tìm khách hàng phương thức POST có phân trang
    @PostMapping("/search")
    public ResponseEntity<PageResponse<CustomerResponse>> searchCustomersRequestBody(
            @RequestBody String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(customerServiceV1.searchCustomers(keyword, page, size));
    }

    // @GetMapping("/get-all/patient")
    // public PageResponse<CustomerResponse> getAllCustomerWithPatientInfo(
    // @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    // @RequestParam(value = "size", required = false, defaultValue = "5") int size)
    // {
    // return customerServiceV1.getAllCustomerWithPatientInfo(page, size);
    // }

    // Tìm khách hàng theo email với phương thức GET kèm thông tin hồ sơ khám bệnh
    // @GetMapping("/email/patient/{email}")
    // public CustomerResponse getCustomerByEmailWithPatientInfo(@PathVariable
    // String email) {
    // return customerServiceV1.getCustomerByEmailWithPatientInfo(email);
    // }

    // Tìm khách hàng theo số điện thoại với phương thức GET kèm thông tin hồ sơ
    // khám bệnh
    // @GetMapping("/phone/patient/{phone}")
    // public CustomerResponse getCustomerByPhoneWithPatientInfo(@PathVariable
    // String phone) {
    // return customerServiceV1.getCustomerByPhoneWithPatientInfo(phone);
    // }
}
