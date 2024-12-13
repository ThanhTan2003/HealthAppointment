package com.programmingtechie.appointment_service.repository.httpClient;

import com.programmingtechie.appointment_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameInAppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient(
        name = "his-client",
        url = "${app.services.his}")
public interface HisClient {

}
