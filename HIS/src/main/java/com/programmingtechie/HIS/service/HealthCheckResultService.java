package com.programmingtechie.HIS.service;

import com.programmingtechie.HIS.APIAuthentication.HmacUtils;
import com.programmingtechie.HIS.APIAuthentication.SecretKeys;
import com.programmingtechie.HIS.dto.request.HealthCheckResultRequest;
import com.programmingtechie.HIS.dto.response.FileUploadResponse;
import com.programmingtechie.HIS.dto.response.HealthCheckResultResponse;
import com.programmingtechie.HIS.dto.response.HealthCheckResultsDeletedResponse;
import com.programmingtechie.HIS.dto.response.PageResponse;
import com.programmingtechie.HIS.mapper.HealthCheckResultMapper;
import com.programmingtechie.HIS.mapper.HealthCheckResultsDeletedMapper;
import com.programmingtechie.HIS.minio.integration.MinioChannel;
import com.programmingtechie.HIS.model.Appointment;
import com.programmingtechie.HIS.model.HealthCheckResult;
import com.programmingtechie.HIS.model.HealthCheckResultsDeleted;
import com.programmingtechie.HIS.repository.*;
import com.programmingtechie.HIS.repository.httpClient.AppointmentClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HealthCheckResultService {
    final AppointmentRepository appointmentRepository;
    final HealthCheckResultRepository healthCheckResultRepository;
    final HealthCheckResultsDeletedRepository healthCheckResultsDeletedRepository;

    final HealthCheckResultMapper healthCheckResultMapper;
    final HealthCheckResultsDeletedMapper healthCheckResultsDeletedMapper;

    final MinioChannel minioChannel;

    final HmacUtils hmacUtils;
    final SecretKeys secretKeys;

    final AppointmentClient appointmentClient;

    public void createHealthCheckResult(HealthCheckResultRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId()).orElseThrow(null);
        if(appointment == null)
            throw new IllegalArgumentException("Không tìm thấy lịch hẹn hợp lệ!");

        FileUploadResponse fileUploadResponse = minioChannel.uploadFileWithUUID(request.getFile());

        HealthCheckResult healthCheckResult = HealthCheckResult.builder()
                .appointment(appointment)
                .name(request.getName())
                .fileName(fileUploadResponse.getFileName())
                .bucketName(fileUploadResponse.getBucketName())
                .build();

        log.info("LUU KET QUA VAO CSDL : " + healthCheckResult.toString());
        healthCheckResultRepository.save(healthCheckResult);

        log.info("YEU CAU APPOINTMENT DONG DO");
        log.info("thoi gian cua ket qua");
        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(15);

        List<String> params = new ArrayList<>();
        params.add(expiryDateTime.toString());

        String message = hmacUtils.createMessage(params);

        String hmac = "";
        try{
            hmac = hmacUtils.generateHmac(message, secretKeys.getAppointmentSecretKey());
        } catch (Exception e) {log.info(e.getMessage());}

        try{
            log.info("Thoi gian gui: " + LocalDateTime.now().toString());
           appointmentClient.syncHealthCheckResultFromHis(expiryDateTime, hmac);
            log.info("DA XONG");
        } catch (Exception e) {
            log.info("XAY RA LOI.................................");
            log.info(e.getMessage());
        }


    }

    public String generateFileUrl(String fileName, String bucketName) {
        String URL = minioChannel.generateFileUrl(fileName, bucketName);
        return URL;
    }

    public Void deleteFile(String fileName, String bucketName) {
        minioChannel.deleteFile(fileName);

        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(15);

        List<String> params = new ArrayList<>();
        params.add(expiryDateTime.toString());

        String message = hmacUtils.createMessage(params);

        String hmac = "";
        try{
            hmac = hmacUtils.generateHmac(message, secretKeys.getAppointmentSecretKey());
        } catch (Exception e) {log.info(e.getMessage());}

        try{
            appointmentClient.syncHealthCheckResultFromHis(expiryDateTime, hmac);
        } catch (Exception e) {log.info(e.getMessage());}

        return null;
    }

    public Void deleteHealthCheckResult(String id) {
        HealthCheckResult healthCheckResult = healthCheckResultRepository.findById(id).orElseThrow(null);
        if(healthCheckResult == null)
            return null;

        String fileName = healthCheckResult.getFileName();
        String bucketName = healthCheckResult.getBucketName();

        log.info("TIEN HANH XOA TRONG MINIO");
        minioChannel.deleteFile(fileName);

        log.info("TIEN HANH XOA TRONG CSDL");
        healthCheckResultRepository.delete(healthCheckResult);

        log.info("LUU KET QUA DA XOA");
        HealthCheckResultsDeleted healthCheckResultsDeleted = HealthCheckResultsDeleted.builder()
                .id(id)
                .build();
        healthCheckResultsDeletedRepository.save(healthCheckResultsDeleted);

        log.info("YEU CAU APPOINTMENT DONG DO");
        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(15);

        List<String> params = new ArrayList<>();
        params.add(expiryDateTime.toString());

        String message = hmacUtils.createMessage(params);

        String hmac = "";
        try{
            hmac = hmacUtils.generateHmac(message, secretKeys.getAppointmentSecretKey());
        } catch (Exception e) {log.info(e.getMessage());}

        try{
            appointmentClient.syncHealthCheckResultFromHis(expiryDateTime, hmac);
        } catch (Exception e) {log.info(e.getMessage());}

        log.info("HOAN THANH");
        return null;
    }

    public PageResponse<HealthCheckResultResponse> getHealthCheckResultResponseForMedicalAppointmentSystem(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime expiryDateTime, Integer page, Integer size, String hmac) {
        // Kiểm tra nếu expiryDateTime đã qua thời gian hiện tại
        if (expiryDateTime.isBefore(LocalDateTime.now())) {
            log.error("Request expired! Expiry time: " + expiryDateTime + " Current time: " + LocalDateTime.now());
            throw new IllegalArgumentException("Yêu cầu đã quá hạn!");
        }

        // Kiểm tra page có hợp lệ không
        if (page <= 0) {
            throw new IllegalArgumentException("Page must be greater than 0.");
        }

        // Tạo params và message
        List<String> params = new ArrayList<>();
        params.add(startDate.toString());
        params.add(endDate.toString());
        params.add(expiryDateTime.toString());
        params.add(page.toString());
        params.add(size.toString());

        String message = hmacUtils.createMessage(params);

        // Kiểm tra HMAC
        Boolean isCkeck = false;
        try {
            isCkeck = hmacUtils.verifyHmac(message, hmac, secretKeys.getHisSecretKey());
        } catch (Exception e) {
            log.error("Error verifying HMAC: ", e);
            throw new IllegalArgumentException("Xác thực HMAC không thành công.");
        }

        log.info("Ket qua xac thuc: " + isCkeck);
        if (!isCkeck) {
            throw new IllegalArgumentException("Xác thực HMAC không thành công.");
        }

        // Khởi tạo Pageable
        Pageable pageable = PageRequest.of(page - 1, size);

        log.info("start: " + startDate.toString());
        log.info("end: " + endDate.toString());

        // Lấy danh sách các Appointment thỏa mãn điều kiện
        Page<HealthCheckResult> healthCheckResults = healthCheckResultRepository.findByLastUpdatedBetween(startDate, endDate, pageable);

        // Chuyển đổi Appointment thành AppointmentSyncResponse
        List<HealthCheckResultResponse> data = healthCheckResults.stream()
                .map(healthCheckResultMapper::toHealthCheckResultResponse)
                .collect(Collectors.toList());

        // Log thông tin về dữ liệu
        log.info("Total appointments: " + data.size());
        log.info("Total pages: " + healthCheckResults.getTotalPages());

        // Trả về PageResponse
        return PageResponse.<HealthCheckResultResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(healthCheckResults.getTotalPages())
                .totalElements(healthCheckResults.getTotalElements())
                .data(data)
                .build();
    }

    public Void deletedHealthCheckResultsDeletedForMedicalAppointmentSystem(LocalDateTime endDate, LocalDateTime expiryDateTime, String hmac) {
        // Kiểm tra nếu expiryDateTime đã qua thời gian hiện tại
        if (expiryDateTime.isBefore(LocalDateTime.now())) {
            log.error("Request expired! Expiry time: " + expiryDateTime + " Current time: " + LocalDateTime.now());
            throw new IllegalArgumentException("Yêu cầu đã quá hạn!");
        }


        // Tạo params và message
        List<String> params = new ArrayList<>();
        params.add(endDate.toString());
        params.add(expiryDateTime.toString());

        String message = hmacUtils.createMessage(params);

        // Kiểm tra HMAC
        Boolean isCkeck = false;
        try {
            isCkeck = hmacUtils.verifyHmac(message, hmac, secretKeys.getHisSecretKey());
        } catch (Exception e) {
            log.error("Error verifying HMAC: ", e);
            throw new IllegalArgumentException("Xác thực HMAC không thành công.");
        }

        log.info("Ket qua xac thuc: " + isCkeck);
        if (!isCkeck) {
            throw new IllegalArgumentException("Xác thực HMAC không thành công.");
        }
        log.info("Xoa du thua ------------------");
        healthCheckResultsDeletedRepository.deleteByLastUpdatedBefore(endDate);
        return null;
    }

    public String generateFileUrlPublic(String fileName, String bucketName, LocalDateTime expiryDateTime, String hmac) {
        // Kiểm tra nếu expiryDateTime đã qua thời gian hiện tại
        if (expiryDateTime.isBefore(LocalDateTime.now())) {
            log.error("Request expired! Expiry time: " + expiryDateTime + " Current time: " + LocalDateTime.now());
            throw new IllegalArgumentException("Yêu cầu đã quá hạn!");
        }

        // Tạo params và message
        List<String> params = new ArrayList<>();
        params.add(fileName);
        params.add(bucketName);
        params.add(expiryDateTime.toString());

        String message = hmacUtils.createMessage(params);

        // Kiểm tra HMAC
        Boolean isCkeck = false;
        try {
            isCkeck = hmacUtils.verifyHmac(message, hmac, secretKeys.getHisSecretKey());
        } catch (Exception e) {
            log.error("Error verifying HMAC: ", e);
            throw new IllegalArgumentException("Xác thực HMAC không thành công.");
        }

        log.info("Ket qua xac thuc: " + isCkeck);
        if (!isCkeck) {
            throw new IllegalArgumentException("Xác thực HMAC không thành công.");
        }
        return minioChannel.generateFileUrl(fileName, bucketName);
    }

    public PageResponse<HealthCheckResultsDeletedResponse> getHealthCheckResultsDeletedForMedicalAppointmentSystem(LocalDateTime endDate, LocalDateTime expiryDateTime, Integer page, Integer size, String hmac) {
        // Kiểm tra nếu expiryDateTime đã qua thời gian hiện tại
        if (expiryDateTime.isBefore(LocalDateTime.now())) {
            log.error("Request expired! Expiry time: " + expiryDateTime + " Current time: " + LocalDateTime.now());
            throw new IllegalArgumentException("Yêu cầu đã quá hạn!");
        }

        // Tạo params và message
        List<String> params = new ArrayList<>();
        params.add(endDate.toString());
        params.add(expiryDateTime.toString());
        params.add(page.toString());
        params.add(size.toString());


        String message = hmacUtils.createMessage(params);

        // Kiểm tra HMAC
        Boolean isCkeck = false;
        try {
            isCkeck = hmacUtils.verifyHmac(message, hmac, secretKeys.getHisSecretKey());
        } catch (Exception e) {
            log.error("Error verifying HMAC: ", e);
            throw new IllegalArgumentException("Xác thực HMAC không thành công.");
        }

        log.info("Ket qua xac thuc: " + isCkeck);
        if (!isCkeck) {
            throw new IllegalArgumentException("Xác thực HMAC không thành công.");
        }

        // Khởi tạo Pageable
        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy danh sách các Appointment thỏa mãn điều kiện
        Page<HealthCheckResultsDeleted> healthCheckResults = healthCheckResultsDeletedRepository.findByLastUpdatedBefore(endDate, pageable);

        // Chuyển đổi Appointment thành AppointmentSyncResponse
        List<HealthCheckResultsDeletedResponse> data = healthCheckResults.stream()
                .map(healthCheckResultsDeletedMapper::toHealthCheckResultsDeletedResponse)
                .collect(Collectors.toList());

        // Trả về PageResponse
        return PageResponse.<HealthCheckResultsDeletedResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(healthCheckResults.getTotalPages())
                .totalElements(healthCheckResults.getTotalElements())
                .data(data)
                .build();
    }
}
