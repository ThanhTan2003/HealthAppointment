package com.programmingtechie.identity_service.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrospectResponse {
    boolean valid;
}
