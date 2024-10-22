package com.programmingtechie.identity_service.service;

import java.util.HashSet;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.programmingtechie.identity_service.dto.request.UserRequest;
import com.programmingtechie.identity_service.dto.response.UserResponse;
import com.programmingtechie.identity_service.enums.Role;
import com.programmingtechie.identity_service.model.User;
import com.programmingtechie.identity_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;

    public UserResponse createCustomer(UserRequest userRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.NGUOI_DUNG.name());
        User user = User.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);
        UserResponse userResponse = UserResponse.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();

        return userResponse;
    }

    public UserResponse getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Can not find user"));
        UserResponse userResponse = UserResponse.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
        return userResponse;
    }

    public UserResponse getUserInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Can not find user!"));

        UserResponse userResponse = UserResponse.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
        return userResponse;
    }
}
