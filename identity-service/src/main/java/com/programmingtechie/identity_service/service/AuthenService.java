package com.programmingtechie.identity_service.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.programmingtechie.identity_service.dto.request.AuthenRequest;
import com.programmingtechie.identity_service.dto.request.IntrospectRequest;
import com.programmingtechie.identity_service.dto.request.LogoutRequest;
import com.programmingtechie.identity_service.dto.request.RefreshRequest;
import com.programmingtechie.identity_service.dto.response.AuthenResponse;
import com.programmingtechie.identity_service.dto.response.IntrospectResponse;
import com.programmingtechie.identity_service.exception.AppException;
import com.programmingtechie.identity_service.exception.ErrorCode;
import com.programmingtechie.identity_service.model.Customer;
import com.programmingtechie.identity_service.model.InvalidatedToken;
import com.programmingtechie.identity_service.model.User;
import com.programmingtechie.identity_service.repository.CustomerRepository;
import com.programmingtechie.identity_service.repository.InvalidatedTokenRepository;
import com.programmingtechie.identity_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenService {
    final UserRepository userRepository;
    final InvalidatedTokenRepository invalidatedTokenRepository;
    final CustomerRepository customerRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenResponse authenticatedCustomer(AuthenRequest authenRequest) {
        var customer = customerRepository
                .findByEmail(authenRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại!"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean isValid = passwordEncoder.matches(authenRequest.getPassword(), customer.getPassword());

        if (!isValid) {
            throw new IllegalArgumentException("Invalid login");
        }
        var token = generateTokenCustomer(customer);

        return AuthenResponse.builder().token(token).build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    private String generateToken(User user) {
        // Tao phan header cho JWT voi thuat toan ky HS512
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Tao cac claims cho JWT, bao gom thong tin nhu ten nguoi dung, thoi gian phat
        // hanh, thoi gian het han
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName()) // Ten nguoi dung
                .issuer("Health Appointment") // Nguoi phat hanh token
                .issueTime(new Date()) // Thoi gian phat hanh token
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) // Thoi gian het han
                .jwtID(UUID.randomUUID().toString()) // ID cua JWT
                .claim("name", user.getAccountName()) // Them thong tin ve ten nguoi dung
                .claim("scope", buildScope(user)) // Them thong tin ve quyen han cua nguoi dung
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); // Chuyen doi claims thanh doi tuong Payload

        JWSObject jwsObject = new JWSObject(header, payload); // Tao JWT tu header va payload

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes())); // Ky token bang SIGNER_KEY
            return jwsObject.serialize(); // Tra ve chuoi JWT
        } catch (JOSEException e) {
            log.error("Không thể tạo token!", e); // Ghi log neu xay ra loi
            throw new RuntimeException(e);
        }
    }

    public AuthenResponse authenticate(AuthenRequest request) {
        var user = userRepository
                .findByUserName(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Tai khoan khong ton tai!"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); // Ma hoa mat khau
        boolean result = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!result) {
            throw new IllegalArgumentException("Thông tin đăng nhập không hợp lệ!");
        }

        return AuthenResponse.builder().token(generateToken(user)).build();
    }

    private String generateTokenCustomer(Customer customer) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(customer.getEmail())
                .issuer("health.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("name", customer.getFullName())
                .claim("phone_number", customer.getPhoneNumber())
                .claim("email", customer.getEmail())
                .claim("scope", "NguoiDung")
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh) // Lay thoi gian het han cua token
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(36000, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    public AuthenResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var email = signedJWT.getJWTClaimsSet().getSubject();

        var customer =
                customerRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateTokenCustomer(customer);

        return AuthenResponse.builder().token(token).build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (user.getRole() != null && !StringUtils.isEmpty(user.getRole().getId())) {
            stringJoiner.add(user.getRole().getId());
        }

        return stringJoiner.toString();
    }
}
