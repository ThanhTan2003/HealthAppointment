package com.programmingtechie.identity_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.programmingtechie.identity_service.dto.request.UserRequest;
import com.programmingtechie.identity_service.dto.request.UserUpdateRequest;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.dto.response.UserResponse;
import com.programmingtechie.identity_service.model.Role;
import com.programmingtechie.identity_service.model.User;
import com.programmingtechie.identity_service.repository.RoleRepository;
import com.programmingtechie.identity_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final RoleRepository roleRepository;

    public void createUser(UserRequest request) {

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
        return userMapToUserResponse(user);
    }

    public void deleteUser(String userName) {
        if (!userRepository.existsById(userName)) {
            throw new IllegalArgumentException("Không tồn tại tài khoản " + userName + "!");
        }

        userRepository.deleteById(userName);
    }

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

    public UserResponse getInfo() {
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
}
