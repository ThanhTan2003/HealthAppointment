package com.programmingtechie.identity_service.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.programmingtechie.identity_service.dto.request.IntrospectRequest;
import com.programmingtechie.identity_service.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException { // Phuong thuc decode de giai ma token JWT
        try {
            SignedJWT signedJWT = SignedJWT.parse(token); // Parse token de lay SignedJWT

            return new Jwt(token, // Tao doi tuong Jwt
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(), // Lay thoi gian phat hanh token
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(), // Lay thoi gian het han token
                    signedJWT.getHeader().toJSONObject(), // Lay header cua token
                    signedJWT.getJWTClaimsSet().getClaims() // Lay claims cua token
            );

        } catch (ParseException e) { // Xu ly truong hop parse token that bai
            throw new JwtException("Invalid token"); // Nen exception neu token khong hop le
        }
    }
}
