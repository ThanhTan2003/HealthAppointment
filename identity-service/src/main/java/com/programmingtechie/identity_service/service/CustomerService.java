package com.programmingtechie.identity_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.identity_service.dto.request.Customer.CustomerRequest;
import com.programmingtechie.identity_service.dto.response.Customer.CustomerResponse;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.mapper.CustomerMapper;
import com.programmingtechie.identity_service.model.Customer;
import com.programmingtechie.identity_service.model.Role;
import com.programmingtechie.identity_service.repository.CustomerRepository;
import com.programmingtechie.identity_service.repository.RoleRepository;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService {
    final CustomerRepository customerRepository;
    final RoleRepository roleRepository;
    final PasswordEncoder passwordEncoder;

    final CustomerMapper customerMapper;



    public CustomerResponse createCustomer(CustomerRequest request) {
        // Kiểm tra xem tất cả các thông tin cần thiết đã được cung cấp hay chưa
        if (request.getFullName() == null || request.getFullName().trim().isEmpty() ||
                request.getDateOfBirth() == null ||
                request.getGender() == null || request.getGender().trim().isEmpty() ||
                request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty() ||
                request.getEmail() == null || request.getEmail().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin!");
        }

        // Kiểm tra email đã tồn tại
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã có tài khoản!");
        }

        // Kiểm tra số điện thoại (10 chữ số)
        if (!request.getPhoneNumber().isEmpty()) {
            if (!Pattern.matches("\\d{10}", request.getPhoneNumber())) {
                throw new IllegalArgumentException("Số điện thoại không hợp lệ!");
            }
            if (customerRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
                throw new IllegalArgumentException("Số điện thoại đã có tài khoản!");
            }
        }

        // Kiểm tra định dạng email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, request.getEmail())) {
            throw new IllegalArgumentException("Email không hợp lệ!");
        }

        // Kiểm tra mật khẩu (ít nhất 5 ký tự)
        if (request.getPassword().length() < 5) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 5 ký tự!");
        }

        // Kiểm tra vai trò "NguoiDung"
        Optional<Role> role = roleRepository.findById("NguoiDung");
        if (role.isEmpty()) {
            throw new IllegalArgumentException("Vai trò người dùng không tồn tại!");
        }

        // Tạo khách hàng mới
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Mã hóa mật khẩu
                .status("Đang hoạt động")
                .role(role.get())
                .lastUpdated(LocalDateTime.now())
                .build();

        // Lưu khách hàng vào cơ sở dữ liệu
        customerRepository.save(customer);

        // Trả về phản hồi
        return customerMapper.toCustomerResponse(customer);
    }


    public CustomerResponse updateCustomer(String customerId, CustomerRequest request) {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin!"));

        customer.setFullName(request.getFullName());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setEmail(request.getEmail());
        customer.setStatus(request.getStatus());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            customer.setPassword(passwordEncoder.encode(request.getPassword())); // Cập nhật mật khẩu
        }

        customerRepository.save(customer);

        return customerMapper.toCustomerResponse(customer);
    }

    public void deleteCustomer(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("Không tìm thấy thông tin!");
        }
        customerRepository.deleteById(customerId);
    }

    public CustomerResponse findCustomerByEmail(String email) {
        Customer customer = customerRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin với email: " + email));
        return customerMapper.toCustomerResponse(customer);
    }

    public CustomerResponse findCustomerByPhoneNumber(String phoneNumber) {
        Customer customer = customerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tìm thấy thông tin với số điện thoại: " + phoneNumber));
        return customerMapper.toCustomerResponse(customer);
    }

    public List<CustomerResponse> findCustomersByStatus(String status) {
        List<Customer> customers = customerRepository.findByStatus(status);
        return customers.stream().map(customerMapper::toCustomerResponse).toList();
    }

    public CustomerResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Người dùng chưa được xác thực");
        }

        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            // Lấy thông tin từ Jwt
            String id = jwt.getClaim("id");
            if (id == null) {
                throw new IllegalArgumentException("Không tìm thấy ID trong token!");
            }

            // Tìm người dùng trong cơ sở dữ liệu
            Customer customer = customerRepository
                    .findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không xác định được thông tin người dùng!"));

            return customerMapper.toCustomerResponse(customer);
        }

        throw new IllegalArgumentException("Principal không hợp lệ hoặc không phải là JWT");
    }

    public PageResponse<CustomerResponse> getCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("fullName").ascending());
        var pageData = customerRepository.getAllCustomers(pageable);

        List<CustomerResponse> customerResponses = pageData.getContent().stream()
                .map(customerMapper::toCustomerResponse)
                .toList();

        return PageResponse.<CustomerResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(customerResponses)
                .build();
    }

    public CustomerResponse getCustomerById(String id) {
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin với id: " + id));
        return customerMapper.toCustomerResponse(customer);
    }
}
