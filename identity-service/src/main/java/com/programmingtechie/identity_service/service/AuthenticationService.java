package com.programmingtechie.identity_service.service;

import com.programmingtechie.identity_service.dto.request.AuthenticationRequest;
import com.programmingtechie.identity_service.dto.response.AuthenticationResponse;
import com.programmingtechie.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationService {

    final UserRepository userRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại!"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean result = passwordEncoder.matches(request.getPassword(), user.getPassword());
        return AuthenticationResponse.builder()
                .authenticated(result)
                .build();
    }
}
