package com.programmingtechie.identity_service.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.programmingtechie.identity_service.dto.request.Customer.CustomerRequest;
import com.programmingtechie.identity_service.dto.response.Customer.CustomerResponse;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.model.Customer;
import com.programmingtechie.identity_service.model.Role;
import com.programmingtechie.identity_service.repository.CustomerRepository;
import com.programmingtechie.identity_service.repository.InvalidatedTokenRepository;
import com.programmingtechie.identity_service.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceV1 {
    final CustomerRepository customerRepository;
    final InvalidatedTokenRepository invalidatedTokenRepository;
    final RoleRepository roleRepository;

    // Thêm thông tin khách hàng
    public void createCustomer(CustomerRequest customerRequest) {
        if (customerRepository.findByEmail(customerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email đã có tài khoản!");
        }

        if (!customerRequest.getPhoneNumber().isEmpty()) { // Chỉ kiểm tra số điện thoại nếu nó không rỗng
            if (customerRepository.findByPhoneNumber(customerRequest.getPhoneNumber()).isPresent()) {
                throw new IllegalArgumentException("Số điện thoại đã có tài khoản!");
            }
        }
        // validCustomer(customerRequest);
        Optional<Role> role = roleRepository.findById("NguoiDung");
        if (role.isEmpty()) {
            throw new IllegalArgumentException("Vai trò người dùng không tồn tại!");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

       

        Customer customer = Customer.builder()
                .fullName(customerRequest.getFullName())
                .dateOfBirth(customerRequest.getDateOfBirth())
                .gender(customerRequest.getGender())
                .phoneNumber(customerRequest.getPhoneNumber())
                .email(customerRequest.getEmail())
                .password(passwordEncoder.encode(customerRequest.getPassword()))
                .role(role.get())
                .build();

        customerRepository.save(customer);
    }

    // Cập nhật thông tin khách hàng
    public void updateCustomer(String id, CustomerRequest customerRequest) {
        validCustomer(customerRequest);
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        boolean isPhoneNumberExists = customerRepository.existsByPhoneNumber(customerRequest.getPhoneNumber());
        boolean isEmailAlreadyExists = customerRepository.existsByEmail(customerRequest.getEmail());
        if (isEmailAlreadyExists || isPhoneNumberExists) {
            throw new IllegalArgumentException("Điện thoại hoặc email đã tồn tại, vui lòng thử lại.");
        }
        if (optionalCustomer.isPresent()) {
            Customer customer = Customer.builder()
                    .fullName(customerRequest.getFullName())
                    .dateOfBirth(customerRequest.getDateOfBirth())
                    .gender(customerRequest.getGender())
                    .email(customerRequest.getEmail())
                    .phoneNumber(customerRequest.getPhoneNumber())
                    .password(customerRequest.getPassword())
                    .build();

            customerRepository.save(customer);
        } else {
            throw new IllegalArgumentException("Customer with ID " + id + " not found");
        }

    }

    public void deleteCustomer(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Can not find user"));

        customerRepository.delete(customer);
    }

    // Tìm khách hàng theo id
    public CustomerResponse getById(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return mapToCustomerResponse(customer);
    }

    // Tìm khách hàng theo tên
    public List<CustomerResponse> getCustomerByFullName(String name) {
        List<Customer> customers = customerRepository.findByFullName(name);
        return customers.stream().map(this::mapToCustomerResponse).toList();
    }

    // Tìm khách hàng theo email cơ bản
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
        return mapToCustomerResponse(customer);
    }

    // Tìm khách hàng theo số điện thoại cơ bản
    public CustomerResponse getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found with phone number: " + phone));
        return mapToCustomerResponse(customer);
    }

    // Tìm khách hàng theo email với thông tin hồ sơ khám bệnh đầy đủ
    // public CustomerResponse getCustomerByEmailWithPatientInfo(String email) {
    // Customer customer = customerRepository.findByEmail(email)
    // .orElseThrow(() -> new RuntimeException("Customer not found with email: " +
    // email));
    // return mapToCustomerResponseWithPatientInfo(customer);
    // }

    // Tìm khách hàng theo số điện thoại với thông tin hồ sơ khám bệnh đầy đủ
    // public CustomerResponse getCustomerByPhoneWithPatientInfo(String phone) {
    // Customer customer = customerRepository.findByPhoneNumber(phone)
    // .orElseThrow(() -> new RuntimeException("Customer not found with phone: " +
    // phone));
    // return mapToCustomerResponseWithPatientInfo(customer);
    // }

    public PageResponse<CustomerResponse> searchCustomers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customer> pageData = customerRepository.searchCustomers(keyword, pageable);

        return PageResponse.<CustomerResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(this::mapToCustomerResponse)
                        .collect(Collectors.toList()))
                .build();
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
        Date date = customerRequest.getDateOfBirth();
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        String dateOfBirthString = dateFormat.format(date);
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
        if (!customerRequest.getPhoneNumber().matches("^[0-9]{10,}$")) {
            throw new IllegalArgumentException(
                    "Phone number must contain only digits and be at least 12 characters long.");
        }
    }

    // Lấy danh sách khách hàng với thứ tự sắp xếp theo họ tên
    public PageResponse<CustomerResponse> getCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("fullName").ascending());
        var pageData = customerRepository.getAllCustomer(pageable);

        List<CustomerResponse> customerResponses = pageData.getContent().stream()
                .map(this::mapToCustomerResponse)
                .toList();

        return PageResponse.<CustomerResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(customerResponses)
                .build();
    }

    public List<CustomerResponse> findCustomersByStatus(String status) {
        List<Customer> customers = customerRepository.findByStatus(status);
        return customers.stream().map(this::mapToCustomerResponse).toList();
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

    public CustomerResponse getInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        Customer customer = customerRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Không xác định được thông tin!"));

        return mapToCustomerResponse(customer);
    }

    // Lấy danh sách khách hàng với thứ tự chưa sắp xếp kèm thông tin bệnh nhân
    // public PageResponse<CustomerResponse> getAllCustomerWithPatientInfo(int page,
    // int size) {
    // // Tạo Pageable với page và size đầu vào
    // Pageable pageable = PageRequest.of(page - 1, size);

    // // Lấy dữ liệu trang từ repository (lưu ý: kiểu Pageable phải từ
    // // org.springframework, không
    // // phải AWT)
    // Page<Customer> pageData = customerRepository.findAll(pageable);

    // // Mapping dữ liệu từ entity Customer sang DTO CustomerResponse
    // List<CustomerResponse> customerResponses = pageData.getContent().stream()
    // .map(this::mapToCustomerResponseWithPatientInfo)
    // .toList();

    // // Trả về đối tượng PageResponse với các thông tin cần thiết
    // return PageResponse.<CustomerResponse>builder()
    // .currentPage(page)
    // .pageSize(pageData.getSize())
    // .totalPages(pageData.getTotalPages())
    // .totalElements(pageData.getTotalElements())
    // .data(customerResponses)
    // .build();
    // }

    // Hàm mapping dữ liệu từ entity Customer sang DTO CustomerResponse với thông
    // tin bệnh nhân
    // private CustomerResponse mapToCustomerResponseWithPatientInfo(Customer
    // customer) {
    // return CustomerResponse.builder()
    // .id(customer.getId())
    // .fullName(customer.getFullName())
    // .dateOfBirth(customer.getDateOfBirth())
    // .gender(customer.getGender())
    // .phoneNumber(customer.getPhoneNumber())
    // .email(customer.getEmail())
    // .status(customer.getStatus())
    // .lastUpdated(customer.getLastUpdated())
    // .patient(customer.getPatients().stream()
    // .map(this::mapToPatientResponse)
    // .collect(Collectors.toList()))
    // .build();
    // }

    // Hàm mapping dữ liệu từ entity Patient sang DTO PatientResponse
    // private PatientResponse mapToPatientResponse(Patient patient) {
    // return PatientResponse.builder()
    // .id(patient.getId())
    // .fullName(patient.getFullName())
    // .dateOfBirth(patient.getDateOfBirth())
    // .gender(patient.getGender())
    // .insuranceId(patient.getInsuranceId())
    // .identificationCodeOrPassport(patient.getIdentificationCodeOrPassport())
    // .nation(patient.getNation())
    // .occupation(patient.getOccupation())
    // .phoneNumber(patient.getPhoneNumber())
    // .email(patient.getEmail())
    // .country(patient.getCountry())
    // .province(patient.getProvince())
    // .district(patient.getDistrict())
    // .ward(patient.getWard())
    // .address(patient.getAddress())
    // .relationship(patient.getRelationship())
    // .note(patient.getNote())
    // .lastUpdated(patient.getLastUpdated())
    // .build();
    // }
}
