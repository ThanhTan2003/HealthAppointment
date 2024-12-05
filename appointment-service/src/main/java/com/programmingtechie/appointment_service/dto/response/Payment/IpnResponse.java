package com.programmingtechie.appointment_service.dto.response.Payment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IpnResponse {
    @JsonProperty("RspCode")
    private String responseCode;

    @JsonProperty("Message")
    private String message;
}
