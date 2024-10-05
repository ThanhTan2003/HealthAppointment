package com.programmingtechie.identity_service.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.programmingtechie.identity_service.dto.request.AuthenticationRequest;
import com.programmingtechie.identity_service.dto.request.IntrospectRequest;
import com.programmingtechie.identity_service.dto.request.LogOutRequest;
import com.programmingtechie.identity_service.dto.request.RefreshRequest;
import com.programmingtechie.identity_service.dto.response.AuthenticationResponse;
import com.programmingtechie.identity_service.dto.response.IntrospectResponse;
import com.programmingtechie.identity_service.exception.AppException;
import com.programmingtechie.identity_service.exception.ErrorCode;
import com.programmingtechie.identity_service.model.InvalidatedToken;
import com.programmingtechie.identity_service.model.User;
import com.programmingtechie.identity_service.repository.InvalidatedTokenRepository;
import com.programmingtechie.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;


// Dich vu nay cung cap cac chuc nang lien quan den xac thuc trong he thong
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthenticationService {

    final UserRepository userRepository;
    final InvalidatedTokenRepository invalidatedTokenRepository;

    // Cac gia tri lien quan den JWT (key ky, thoi gian hieu luc, thoi gian lam moi token)
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    // Phuong thuc tao token moi cho nguoi dung
    private String generateToken(User user) {
        // Tao phan header cho JWT voi thuat toan ky HS512
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Tao cac claims cho JWT, bao gom thong tin nhu ten nguoi dung, thoi gian phat hanh, thoi gian het han
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName()) // Ten nguoi dung
                .issuer("Health Appointment") // Nguoi phat hanh token
                .issueTime(new Date()) // Thoi gian phat hanh token
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli())) // Thoi gian het han
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
            log.error("Cannot create token", e); // Ghi log neu xay ra loi
            throw new RuntimeException(e);
        }
    }

    // Phuong thuc dang nhap
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("Tai khoan khong ton tai!")); // Kiem tra tai khoan co ton tai khong

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); // Ma hoa mat khau
        boolean result = passwordEncoder.matches(request.getPassword(), user.getPassword()); // Kiem tra mat khau
        return AuthenticationResponse.builder()
                .authenticated(result) // Tra ve ket qua dang nhap
                .token(generateToken(user)) // Tra ve token neu dang nhap thanh cong
                .build();
    }

    // Phuong thuc kiem tra token
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false); // Kiem tra token
        } catch (AppException e) {
            isValid = false; // Neu token khong hop le, tra ve false
        }

        return IntrospectResponse.builder().valid(isValid).build(); // Tra ve ket qua kiem tra
    }

    // Phuong thuc lay quyen (role) cua nguoi dung
    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (user.getRole() != null && !StringUtils.isEmpty(user.getRole().getId())) {
            stringJoiner.add(user.getRole().getId()); // Them ID cua quyen vao chuoi
        }

        return stringJoiner.toString(); // Tra ve chuoi cac quyen
    }

    // Phuong thuc kiem tra tinh hop le cua token
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes()); // Xac thuc token bang SIGNER_KEY

        SignedJWT signedJWT = SignedJWT.parse(token); // Phan tich token

        Date expiryTime = (isRefresh) // Lay thoi gian het han cua token
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier); // Xac thuc token

        // Kiem tra xem token da het han hoac bi vo hieu hoa chua
        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT; // Tra ve token da xac thuc
    }

    // Phuong thuc dang xuat
    public void logOut(LogOutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true); // Xac thuc token khi dang xuat

            String jit = signToken.getJWTClaimsSet().getJWTID(); // Lay ID cua token
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime(); // Lay thoi gian het han

            // Luu token vao danh sach cac token da vo hieu hoa
            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder()
                            .id(jit)
                            .expiryTime(expiryTime)
                            .build();

            invalidatedTokenRepository.save(invalidatedToken); // Luu vao co so du lieu
        } catch (AppException exception){
            log.info("Token already expired"); // Ghi log neu token da het han
        }
    }

    // Phuong thuc lam moi token
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true); // Xac thuc token

        var jit = signedJWT.getJWTClaimsSet().getJWTID(); // Lay ID cua token
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime(); // Lay thoi gian het han

        // Luu token cu vao danh sach cac token da vo hieu hoa
        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject(); // Lay ten nguoi dung tu token

        var user =
                userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED)); // Tim nguoi dung trong co so du lieu

        var token = generateToken(user); // Tao token moi

        return AuthenticationResponse.builder().token(token).authenticated(true).build(); // Tra ve token moi va ket qua xac thuc
    }
}
