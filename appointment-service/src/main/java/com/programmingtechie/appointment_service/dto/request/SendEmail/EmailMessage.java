package com.programmingtechie.appointment_service.dto.request.SendEmail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmailMessage {
    private String to;

    private String subject;

    private String message;

}
