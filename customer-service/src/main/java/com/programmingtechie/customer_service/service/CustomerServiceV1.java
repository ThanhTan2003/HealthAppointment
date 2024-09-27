package com.programmingtechie.customer_service.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.programmingtechie.customer_service.dto.request.CustomerRequest;
import com.programmingtechie.customer_service.dto.response.CustomerResponse;
import com.programmingtechie.customer_service.dto.response.PageResponse;
import com.programmingtechie.customer_service.model.Customer;
import com.programmingtechie.customer_service.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceV1 {
    final CustomerRepository customerRepository;

    public void createCustomer(CustomerRequest customerRequest) {
        validCustomer(customerRequest);
        Customer customer = Customer.builder()
                .fullName(customerRequest.getFullName())
                .dateOfBirth(customerRequest.getDateOfBirth())
                .gender(customerRequest.getGender())
                .email(customerRequest.getEmail())
                .phoneNumber(customerRequest.getPhoneNumber())
                .password(customerRequest.getPassword())
                .build();

        customerRepository.save(customer);
    }

    void validCustomer(CustomerRequest customerRequest) {
        if (customerRequest.getFullName() == null || customerRequest.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        if (customerRequest.getEmail() == null || customerRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Your email cannot be empty");
        }
        if (customerRequest.getGender() == null || customerRequest.getGender().isEmpty()) {
            throw new IllegalArgumentException("Your gender cannot be empty");
        }
        if (customerRequest.getPhoneNumber() == null || customerRequest.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Your phone number cannot be empty");
        }

        // Kiểm tra ngày tháng năm sinh không được bỏ trống
        String dateOfBirthString = customerRequest.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (dateOfBirthString == null || dateOfBirthString.isEmpty()) {
            throw new IllegalArgumentException("Your date of birth cannot be empty");
        }

        // Kiểm tra password có ít nhất 1 ký tự in hoa, 1 ký tự đặc biệt và ít nhất 8 ký
        // tự
        if (!customerRequest.getPassword().matches("^(?=.*[A-Z])(?=.*[!@#$%^&*()-+=])(?=\\S+$).{8,}$")) {
            throw new IllegalArgumentException(
                    "Password must contain at least one uppercase letter, one special character, and be at least 8 characters long.");
        }

        // Kiểm tra phoneNumber có định dạng số và ít nhất 12 ký tự
        if (!customerRequest.getPhoneNumber().matches("^[0-9]{12,}$")) {
            throw new IllegalArgumentException(
                    "Phone number must contain only digits and be at least 12 characters long.");
        }
    }

    public PageResponse<CustomerResponse> getAllCustomer(int page, int size) {
        // Tạo đối tượng Sort theo thứ tự tăng dần của fullName
        Sort sort = Sort.by("fullName").ascending();

        // Tạo Pageable với page và size đầu vào
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // Lấy dữ liệu trang từ repository (lưu ý: kiểu Pageable phải từ Spring, không
        // phải AWT)
        var pageData = customerRepository.getAllCustomer(pageable);

        // Mapping dữ liệu từ entity Customer sang DTO CustomerResponse
        List<CustomerResponse> customerResponses = pageData.getContent().stream()
                .map(doctor -> CustomerResponse.builder()
                        .id(doctor.getId())
                        .fullName(doctor.getFullName())
                        .dateOfBirth(doctor.getDateOfBirth())
                        .gender(doctor.getGender())
                        .phoneNumber(doctor.getPhoneNumber())
                        .email(doctor.getEmail())
                        .status(doctor.getStatus())
                        .lastUpdated(doctor.getLastUpdated())
                        .build())
                .toList();

        // Trả về đối tượng PageResponse với các thông tin cần thiết
        return PageResponse.<CustomerResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(customerResponses)
                .build();
    }
}
