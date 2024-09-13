package com.programmingtechie.identity_service.service;

import com.programmingtechie.identity_service.dto.request.UserCreationRequest;
import com.programmingtechie.identity_service.dto.request.UserUpdateRequest;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.dto.response.UserResponse;
import com.programmingtechie.identity_service.model.User;
import com.programmingtechie.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    final UserRepository userRepository;
    final WebClient.Builder webClientBuilder;

    private UserResponse userMapToUserResponse(User user) {
        return UserResponse.builder()
                .userName(user.getUserName())
                .password(user.getPassword())
                .accountName(user.getAccountName())
                .status(user.getStatus())
                .lastAccessTime(user.getLastAccessTime())
//                .accountTypeId(user.getAccountTypeId())
                .doctorId(user.getDoctorId())
                .roles(user.getRoles())
                .build();
    }

    public void createUser(UserCreationRequest request) {

        if (userRepository.existsByUserName(request.getUserName()))
            throw new IllegalArgumentException("Tài khoản đã tồn tại!");

        User user = User.builder()
                .userName(request.getUserName())
                .accountName(request.getAccountName())
                .status(request.getStatus())
//                .accountTypeId(request.getAccountTypeId())
                .doctorId(request.getDoctorId())
                .roles(request.getRoles())
                .build();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    public PageResponse<UserResponse> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("userName").ascending());
        var pageData = userRepository.getAllUser(pageable);

        List<UserResponse> userResponses = pageData.getContent().stream()
                .map(this::userMapToUserResponse)
                .toList();

        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(userResponses)
                .build();
    }

    public UserResponse getUserByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tồn tại tài khoản " + userId + "!"));
        return userMapToUserResponse(user);
    }

    public void updateUser(UserUpdateRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("Không tồn tại tài khoản " + request.getUserName() +"!"));

        user.setAccountName(request.getAccountName());
        user.setStatus(request.getStatus());
        user.setDoctorId(request.getDoctorId());
        user.setRoles(request.getRoles());

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
}
