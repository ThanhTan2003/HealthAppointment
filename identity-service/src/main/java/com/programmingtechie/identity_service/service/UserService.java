package com.programmingtechie.identity_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.programmingtechie.identity_service.dto.response.Doctor.DoctorResponse;
import com.programmingtechie.identity_service.mapper.UserMapper;
import com.programmingtechie.identity_service.repository.httpClient.DoctorClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.programmingtechie.identity_service.dto.request.UserCreationRequest;
import com.programmingtechie.identity_service.dto.request.UserUpdateRequest;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.dto.response.UserResponse;
import com.programmingtechie.identity_service.model.Role;
import com.programmingtechie.identity_service.model.User;
import com.programmingtechie.identity_service.repository.RoleRepository;
import com.programmingtechie.identity_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    final UserRepository userRepository;
    final RoleRepository roleRepository;

    final UserMapper userMapper;

    final WebClient.Builder webClientBuilder;

    final DoctorClient doctorClient;

    private UserResponse userMapToUserResponse(User user) {
        return UserResponse.builder()
                .userName(user.getUserName())
                .password(user.getPassword())
                .accountName(user.getAccountName())
                .status(user.getStatus())
                .lastAccessTime(user.getLastAccessTime())
                .doctorId(user.getDoctorId())
                .roleId(user.getRole().getId())
                .roleName(user.getRole().getName())
                .build();
    }

    public void createUser(UserCreationRequest request) {

        if (request.getUserName() == null || request.getUserName().isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được để trống!");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống!");
        }

        if (request.getAccountName() == null || request.getAccountName().isEmpty()) {
            throw new IllegalArgumentException("Tên tài khoản không được để trống!");
        }

        if (request.getRoleId() == null || request.getRoleId().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn loại tài khoản!");
        }

        if (userRepository.existsByUserName(request.getUserName()))
            throw new IllegalArgumentException("Tài khoản đã tồn tại!");

        Optional<Role> role = roleRepository.findById(request.getRoleId());

        if (role.isEmpty())
            throw new IllegalArgumentException("Không thể thực hiện phân quyền, vui lòng kiểm tra lại!");

        User user = User.builder()
                .userName(request.getUserName())
                .accountName(request.getAccountName())
                .status(request.getStatus())
                .doctorId(request.getDoctorId())
                .role(role.get())
                .build();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    public PageResponse<UserResponse> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("userName").ascending());
        var pageData = userRepository.getAllUser(pageable);

        List<UserResponse> userResponses =
                pageData.getContent().stream().map(this::userMapToUserResponse).toList();

        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(userResponses)
                .build();
    }

    public UserResponse getUserByUserId(String userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tồn tại tài khoản " + userId + "!"));
        UserResponse userResponse = userMapToUserResponse(user);

//        if(!user.getDoctorId().isEmpty())
//        {
//            DoctorResponse doctorResponse = doctorClient.getDoctorById(user.getDoctorId());
//            userResponse.setDoctorResponse(doctorResponse);
//        }
        return userMapToUserResponse(user);
    }

    public void updateUser(UserUpdateRequest request) {
        User user = userRepository
                .findByUserName(request.getUserName())
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tồn tại tài khoản " + request.getUserName() + "!"));

        user.setPassword(request.getPassword());
        user.setStatus(request.getStatus());
        user.setDoctorId(request.getDoctorId());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
    }

    public void deleteUser(String userName) {
        if (!userRepository.existsById(userName)) {
            throw new IllegalArgumentException("Không tồn tại tài khoản " + userName + "!");
        }

        userRepository.deleteById(userName);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository
                .findByUserName(name)
                .orElseThrow(() -> new IllegalArgumentException("Không xác định được thông tin!"));

        return userMapToUserResponse(user);
    }

    public void updatePassword(UserUpdateRequest request) {
        User user = userRepository
                .findByUserName(request.getUserName())
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tồn tại tài khoản " + request.getUserName() + "!"));

        user.setPassword(request.getPassword());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
    }

    public PageResponse<UserResponse> searchUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> pageData = userRepository.searchServices(keyword, pageable);

        List<UserResponse> userResponses = pageData.getContent().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(userResponses)
                .build();
    }
}
