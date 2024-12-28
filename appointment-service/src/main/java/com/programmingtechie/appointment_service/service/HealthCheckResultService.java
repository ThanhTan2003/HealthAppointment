package com.programmingtechie.appointment_service.service;

import com.programmingtechie.appointment_service.APIAuthentication.HmacUtils;
import com.programmingtechie.appointment_service.APIAuthentication.SecretKeys;
import com.programmingtechie.appointment_service.dto.response.AppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.His.HealthCheckResultResponse;
import com.programmingtechie.appointment_service.dto.response.His.HealthCheckResultsDeletedResponse;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.model.Appointment;
import com.programmingtechie.appointment_service.model.HealthCheckResult;
import com.programmingtechie.appointment_service.model.SyncHistory;
import com.programmingtechie.appointment_service.repository.AppointmentRepository;
import com.programmingtechie.appointment_service.repository.HealthCheckResultRepository;
import com.programmingtechie.appointment_service.repository.SyncHistoryRepository;
import com.programmingtechie.appointment_service.repository.httpClient.HisClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.programmingtechie.appointment_service.enums.Sync_History;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthCheckResultService {
    final HealthCheckResultRepository healthCheckResultRepository;
    final SyncHistoryRepository syncHistoryRepository;
    final AppointmentRepository appointmentRepository;

    final HisClient hisClient;

    final HmacUtils hmacUtils;
    final SecretKeys secretKeys;



    public Void syncHealthCheckResult() {
        String appointmentValue = Sync_History.HEALTH_CHECK_RESULTS.getName();
        log.info("id: " + appointmentValue);
        SyncHistory syncHistory = syncHistoryRepository.findById(appointmentValue).orElse(null);
        if(syncHistory == null)
            throw new IllegalArgumentException("Đã xảy ra lỗi khi đồng bộ. Vui lòng kiểm tra và thử lại sau!");
        log.info(syncHistory.toString());

        LocalDateTime startDate = syncHistory.getDateTime();

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime expiryDateTime = endDate.plusMinutes(15);

        Integer page = 1;
        Integer size = 50;

        List<String> params = new ArrayList<>();
        params.add(startDate.toString());
        params.add(endDate.toString());
        params.add(expiryDateTime.toString());
        params.add(page.toString());
        params.add(size.toString());

        String message = hmacUtils.createMessage(params);

        String Hmac = "";
        log.info("----------1. Lay du lieu ve------------");
        try {
            Hmac = hmacUtils.generateHmac(message, secretKeys.getHisSecretKey());
        } catch (Exception e) {
            log.error("Error generating HMAC", e);
        }
        log.info("message: " + message);
        log.info("appointment.secretKey: " + secretKeys.getHisSecretKey());
        log.info("Hmac: " + Hmac);

        PageResponse<HealthCheckResultResponse> pageResponse = null;
        try{
            pageResponse = hisClient.getHealthCheckResultResponseForMedicalAppointmentSystem(startDate, endDate, expiryDateTime, page, size, Hmac);
            log.info("Du lieu: " + pageResponse.getData().toString());
            saveHealthCheckResultForMedicalAppointmentSystem(pageResponse.getData());
        }
        catch (Exception e)
        {
            log.error(e.getMessage().toString());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
        }

        // Kiểm tra totalPages để đảm bảo không lặp lại
        int totalPages = pageResponse.getTotalPages();
        log.info("page: " + page);
        log.info("totalPages: " + totalPages);

        // Lặp qua tất cả các trang nếu có
        while (page < totalPages) {
            log.info("Fetching data for page " + page + " of " + totalPages);

            // Nếu còn trang tiếp theo
            page++;
            log.info("page: " + page);
            params.set(3, String.valueOf(page)); // Cập nhật lại page trong params
            message = hmacUtils.createMessage(params);
            try {
                Hmac = hmacUtils.generateHmac(message, secretKeys.getHisSecretKey());
            } catch (Exception e) {
                log.error("Error generating HMAC for page " + page, e);
            }
            //log.info("message: " + message);
            //log.info("Hmac: " + Hmac);

            // Gửi yêu cầu tiếp theo
            try{
                pageResponse = hisClient.getHealthCheckResultResponseForMedicalAppointmentSystem(startDate, endDate, expiryDateTime, page, size, Hmac);
                saveHealthCheckResultForMedicalAppointmentSystem(pageResponse.getData());
            }
            catch (Exception e)
            {
                log.error(e.getMessage().toString());
                throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
            }
            totalPages = pageResponse.getTotalPages(); // Cập nhật lại totalPages
        }
        syncHistory.setDateTime(endDate);

        log.info("----------2. deleteHealthCheckResults------------");
        deleteHealthCheckResults(endDate, expiryDateTime);

        log.info("----------3. deleteHealthCheckResultsToHis------------");
        deleteHealthCheckResultsToHis(endDate,expiryDateTime);
        syncHistoryRepository.save(syncHistory);
        return null;
    }

    public Void syncHealthCheckResultFromHIS(LocalDateTime expiryDateTime, String hmac) {
        // Kiểm tra nếu expiryDateTime đã qua thời gian hiện tại
        if (expiryDateTime.isBefore(LocalDateTime.now())) {
            log.error("Request expired! Expiry time: " + expiryDateTime + " Current time: " + LocalDateTime.now());
            throw new IllegalArgumentException("Yêu cầu đã quá hạn!");
        }
        log.info("NHAN YEU CAU DONG BO DU LIEU KET QUA KHAM BENH TU HIS");

        List<String> params = new ArrayList<>();
        params.add(expiryDateTime.toString());

        String message = hmacUtils.createMessage(params);

        // Kiểm tra HMAC
        Boolean isCkeck = false;
        try {
            isCkeck = hmacUtils.verifyHmac(message, hmac, secretKeys.getAppointmentSecretKey());
        } catch (Exception e) {
            log.error("Error verifying HMAC: ", e);
            throw new IllegalArgumentException("Xác thực HMAC không thành công.");
        }

        log.info("Ket qua xac thuc: " + isCkeck);
        if (!isCkeck) {
            throw new IllegalArgumentException("Xác thực HMAC không thành công.");
        }
        try{

            syncHealthCheckResult();
        } catch (Exception e)
        {
            log.info(e.getMessage());
        }
        return null;
    }

    public void deleteHealthCheckResults(LocalDateTime endDate, LocalDateTime expiryDateTime)
    {
        Integer page = 1;
        Integer size = 50;

        List<String> params = new ArrayList<>();
        params.add(endDate.toString());
        params.add(expiryDateTime.toString());
        params.add(page.toString());
        params.add(size.toString());

        String message = hmacUtils.createMessage(params);

        String Hmac = "";
        try {
            Hmac = hmacUtils.generateHmac(message, secretKeys.getHisSecretKey());
        } catch (Exception e) {
            log.error("Error generating HMAC", e);
        }
        try{
            log.info("Xoa du thua ------------------");
            hisClient.getHealthCheckResultsDeletedForMedicalAppointmentSystem(endDate, expiryDateTime, page, size, Hmac);
        }
        catch (Exception e)
        {
            log.error(e.getMessage().toString());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
        }

        PageResponse<HealthCheckResultsDeletedResponse> pageResponse = null;
        try{
            pageResponse = hisClient.getHealthCheckResultsDeletedForMedicalAppointmentSystem(endDate, expiryDateTime, page, size, Hmac);
            deleteHealthCheckResultForMedicalAppointmentSystem(pageResponse.getData());
        }
        catch (Exception e)
        {
            log.error(e.getMessage().toString());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
        }

        // Kiểm tra totalPages để đảm bảo không lặp lại
        int totalPages = pageResponse.getTotalPages();
        log.info("page: " + page);
        log.info("totalPages: " + totalPages);

        // Lặp qua tất cả các trang nếu có
        while (page < totalPages) {
            log.info("Fetching data for page " + page + " of " + totalPages);

            // Nếu còn trang tiếp theo
            page++;
            log.info("page: " + page);
            params.set(3, String.valueOf(page)); // Cập nhật lại page trong params
            message = hmacUtils.createMessage(params);
            try {
                Hmac = hmacUtils.generateHmac(message, secretKeys.getHisSecretKey());
            } catch (Exception e) {
                log.error("Error generating HMAC for page " + page, e);
            }
            //log.info("message: " + message);
            //log.info("Hmac: " + Hmac);

            // Gửi yêu cầu tiếp theo
            try{
                pageResponse = hisClient.getHealthCheckResultsDeletedForMedicalAppointmentSystem(endDate, expiryDateTime, page, size, Hmac);
                deleteHealthCheckResultForMedicalAppointmentSystem(pageResponse.getData());
            }
            catch (Exception e)
            {
                log.error(e.getMessage().toString());
                throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
            }
            totalPages = pageResponse.getTotalPages(); // Cập nhật lại totalPages
        }
    }

    private void deleteHealthCheckResultForMedicalAppointmentSystem(List<HealthCheckResultsDeletedResponse> data) {
        for(HealthCheckResultsDeletedResponse item: data)
        {
            String id = item.getId();
            HealthCheckResult healthCheckResult = healthCheckResultRepository.findById(id).orElse(null);
            if(healthCheckResult != null)
            {
                healthCheckResultRepository.delete(healthCheckResult);
                Appointment appointment = healthCheckResult.getAppointment();
                if(appointment.getHealthCheckResults().size() <=0)
                {
                    appointment.setStatus("Đã xác nhận");
                }
            }

        }
    }

    public void deleteHealthCheckResultsToHis(LocalDateTime endDate, LocalDateTime expiryDateTime)
    {
        List<String> params = new ArrayList<>();
        params.add(endDate.toString());
        params.add(expiryDateTime.toString());

        String message = hmacUtils.createMessage(params);

        String Hmac = "";
        try {
            Hmac = hmacUtils.generateHmac(message, secretKeys.getHisSecretKey());
        } catch (Exception e) {
            log.error("Error generating HMAC", e);
        }
        try{
            log.info("Xoa du thua ------------------");
            hisClient.deletedHealthCheckResultsDeletedForMedicalAppointmentSystem(endDate, expiryDateTime, Hmac);
        }
        catch (Exception e)
        {
            log.error(e.getMessage().toString());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
        }
    }

    private void saveHealthCheckResultForMedicalAppointmentSystem(List<HealthCheckResultResponse> healthCheckResultResponses) {
        for (HealthCheckResultResponse healthCheckResultResponse : healthCheckResultResponses) {

            Appointment appointment = appointmentRepository.findById(healthCheckResultResponse.getAppointmentId()).orElse(null);

            HealthCheckResult healthCheckResult = HealthCheckResult.builder()
                    .id(healthCheckResultResponse.getId())
                    .appointment(appointment)
                    .name(healthCheckResultResponse.getName())
                    .fileName(healthCheckResultResponse.getFileName())
                    .bucketName(healthCheckResultResponse.getBucketName())
                    .lastUpdated(healthCheckResultResponse.getLastUpdated())
                    .build();

            log.info("Saving healthCheckResult: " + healthCheckResult);

            healthCheckResultRepository.save(healthCheckResult);

            appointment.setStatus("Đã khám");
        }
    }
}
