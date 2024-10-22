package com.programmingtechie.identity_service.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.identity_service.dto.request.Customer.CustomerRequest;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.dto.response.Customer.CustomerResponse;
import com.programmingtechie.identity_service.service.CustomerServiceV1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@RestController
@RequestMapping("/api/v1/identity/customer")
@RequiredArgsConstructor
@CrossOrigin("*")
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
    @GetMapping("/welcome")
    public String getGreeting() {
        return "Welcome to health appointment app";
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createCustomer(@RequestBody CustomerRequest customerRequest) {
        customerServiceV1.createCustomer(customerRequest);
        return "Tạo tài khoản thành công!";
    }

    @PutMapping("/update/{id}")
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
    public PageResponse<CustomerResponse> getAllCustomer(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        return customerServiceV1.getAllCustomer(page, size);
    }

    // @GetMapping("/get-all/patient")
    // public PageResponse<CustomerResponse> getAllCustomerWithPatientInfo(
    //         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    //         @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
    //     return customerServiceV1.getAllCustomerWithPatientInfo(page, size);
    // }

    // Tìm khách hàng theo họ tên với phương thức GET
    @GetMapping("full-name/{name}")
    public CustomerResponse getCustomerByFullName(@PathVariable String name) {
        return customerServiceV1.getCustomerByFullName(name);
    }

    // Tìm khách hàng theo họ tên với phương thức POST
    @PostMapping("/full-name")
    public CustomerResponse getCustomerByFullNamePostMethod(@RequestBody String name) {
        return customerServiceV1.getCustomerByFullName(name);
    }

    // Tìm khách hàng theo email với phương thức GET
    @GetMapping("/email/{email}")
    public CustomerResponse getCustomerByEmail(@PathVariable String email) {
        return customerServiceV1.getCustomerByEmail(email);
    }

    // Tìm khách hàng theo email với phương thức POST
    @PostMapping("/email")
    public CustomerResponse getCustomerByEmailPostMethod(@RequestBody String email) {
        return customerServiceV1.getCustomerByEmail(email);
    }

    // Tìm khách hàng theo số điện thoại với phương thức GET
    @GetMapping("/phone/{phone}")
    public CustomerResponse getCustomerByPhone(@PathVariable String phone) {
        return customerServiceV1.getCustomerByPhone(phone);
    }

    // Tìm khách hàng theo số điện thoại với phương thức POST
    @PostMapping("/phone")
    public CustomerResponse getCustomerByPhonePostMethod(@RequestBody String phone) {
        return customerServiceV1.getCustomerByPhone(phone);
    }

    // Tìm khách hàng theo email với phương thức GET kèm thông tin hồ sơ khám bệnh
    // @GetMapping("/email/patient/{email}")
    // public CustomerResponse getCustomerByEmailWithPatientInfo(@PathVariable String email) {
    //     return customerServiceV1.getCustomerByEmailWithPatientInfo(email);
    // }

    // Tìm khách hàng theo số điện thoại với phương thức GET kèm thông tin hồ sơ
    // khám bệnh
    // @GetMapping("/phone/patient/{phone}")
    // public CustomerResponse getCustomerByPhoneWithPatientInfo(@PathVariable String phone) {
    //     return customerServiceV1.getCustomerByPhoneWithPatientInfo(phone);
    // }

    // Tìm khách hàng theo email với phương thức POST kèm thông tin hồ sơ khám bệnh
    // @PostMapping("/email/patient")
    // public CustomerResponse getCustomerByEmailPostMethodPatientInfo(@RequestBody String email) {
    //     return customerServiceV1.getCustomerByEmailWithPatientInfo(email);
    // }

    // Tìm khách hàng theo số điện thoại với phương thức POST kèm thông tin hồ sơ
    // khám bệnh
    // @PostMapping("/phone/patient")
    // public CustomerResponse getCustomerByPhonePostMethodPatientInfo(@RequestBody String phone) {
    //     return customerServiceV1.getCustomerByPhoneWithPatientInfo(phone);
    // }

    // Tìm khách hàng phương thức GET có phân trang
    @GetMapping("/search")
    public ResponseEntity<PageResponse<CustomerResponse>> searchCustomers(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(customerServiceV1.searchCustomers(keyword, page, size));
    }

    // Tìm khách hàng phương thức GET có phân trang với RequestBody
    @PostMapping("/search")
    public ResponseEntity<PageResponse<CustomerResponse>> searchCustomersRequestBody(
            @RequestBody String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(customerServiceV1.searchCustomers(keyword, page, size));
    }
}
