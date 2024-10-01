package com.programmingtechie.customer_service.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.*;
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

    //Thêm thông tin khách hàng
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

    //Cập nhật thông tin khách hàng
    public void updateCustomer(CustomerRequest customerRequest) {
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

    public void deleteCustomer(String id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can not find user"));

        
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

    // Lấy danh sách khách hàng với thứ tự chưa sắp xếp
    public PageResponse<CustomerResponse> getAllCustomerWithoutSorting(int page, int size) {
        // Tạo Pageable với page và size đầu vào
        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy dữ liệu trang từ repository (lưu ý: kiểu Pageable phải từ
        // org.springframework, không
        // phải AWT)
        Page<Customer> pageData = customerRepository.findAll(pageable);

        // Mapping dữ liệu từ entity Customer sang DTO CustomerResponse
        List<CustomerResponse> customerResponses = pageData.getContent().stream()
                .map(this::mapToCustomerResponse)
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

    // Tìm khách hàng theo id
    public CustomerResponse getById(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return mapToCustomerResponse(customer);
    }

    // Tìm khách hàng theo id
    public CustomerResponse getByFullName(String name) {
        Customer customer = customerRepository.findByFullName(name)
                .orElseThrow(() -> new RuntimeException("Customer not found with name: " + name));
        return mapToCustomerResponse(customer);
    }

    // Hàm mapping dữ liệu từ entity Customer sang DTO CustomerResponse
    private CustomerResponse mapToCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .status(customer.getStatus())
                .lastUpdated(customer.getLastUpdated())
                .build();
    }
}
