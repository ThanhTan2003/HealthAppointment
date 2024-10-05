package com.programmingtechie.customer_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.customer_service.dto.request.CustomerRequest;
import com.programmingtechie.customer_service.dto.response.CustomerResponse;
import com.programmingtechie.customer_service.dto.response.PageResponse;
import com.programmingtechie.customer_service.model.Customer;
import com.programmingtechie.customer_service.service.CustomerServiceV1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {
    final CustomerServiceV1 customerServiceV1;

    @GetMapping("/welcome")
    public String getGreeting() {
        return "Welcome to health appointment app";
    }

    @PostMapping("/login")
    public String loginSystem(@RequestBody String entity) {
        return entity;
    }

    @PostMapping("/register")
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

    @GetMapping("/get-all/patient")
    public PageResponse<CustomerResponse> getAllCustomerWithPatientInfo(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        return customerServiceV1.getAllCustomerWithPatientInfo(page, size);
    }

    //Tìm khách hàng theo email với phương thức GET
    @GetMapping("/email/{email}")
    public CustomerResponse getCustomerByEmail(@PathVariable String email) {
        return customerServiceV1.getCustomerByEmail(email);
    }

    //Tìm khách hàng theo email với phương thức POST
    @PostMapping("/email")
    public CustomerResponse getCustomerByEmailPostMethod(@RequestBody String email) {
        return customerServiceV1.getCustomerByEmail(email);
    }

    //Tìm khách hàng theo số điện thoại với phương thức GET
    @GetMapping("/phone/{phone}")
    public CustomerResponse getCustomerByPhone(@PathVariable String phone) {
        return customerServiceV1.getCustomerByPhone(phone);
    }

    //Tìm khách hàng theo số điện thoại với phương thức POST
    @PostMapping("/phone")
    public CustomerResponse getCustomerByPhonePostMethod(@RequestBody String phone) {
        return customerServiceV1.getCustomerByPhone(phone);
    }

    //Tìm khách hàng theo email với phương thức GET kèm thông tin hồ sơ khám bệnh
    @GetMapping("/email/patient/{email}")
    public CustomerResponse getCustomerByEmailWithPatientInfo(@PathVariable String email) {
        return customerServiceV1.getCustomerByEmailWithPatientInfo(email);
    }

    //Tìm khách hàng theo số điện thoại với phương thức GET kèm thông tin hồ sơ khám bệnh
    @GetMapping("/phone/patient/{phone}")
    public CustomerResponse getCustomerByPhoneWithPatientInfo(@PathVariable String phone) {
        return customerServiceV1.getCustomerByPhoneWithPatientInfo(phone);
    }

    //Tìm khách hàng theo email với phương thức POST kèm thông tin hồ sơ khám bệnh
    @PostMapping("/email/patient")
    public CustomerResponse getCustomerByEmailPostMethodPatientInfo(@RequestBody String email) {
        return customerServiceV1.getCustomerByEmailWithPatientInfo(email);
    }

    //Tìm khách hàng theo số điện thoại với phương thức POST kèm thông tin hồ sơ khám bệnh
    @PostMapping("/phone/patient")
    public CustomerResponse getCustomerByPhonePostMethodPatientInfo(@RequestBody String phone) {
        return customerServiceV1.getCustomerByPhoneWithPatientInfo(phone);
    }
}
