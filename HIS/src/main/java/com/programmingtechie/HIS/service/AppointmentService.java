package com.programmingtechie.HIS.service;

import com.programmingtechie.HIS.APIAuthentication.HmacUtils;
import com.programmingtechie.HIS.APIAuthentication.SecretKeys;
import com.programmingtechie.HIS.dto.request.HealthCheckResultRequest;
import com.programmingtechie.HIS.dto.response.AppointmentResponse;
import com.programmingtechie.HIS.dto.response.AppointmentSyncResponse;
import com.programmingtechie.HIS.dto.response.FileUploadResponse;
import com.programmingtechie.HIS.dto.response.PageResponse;
import com.programmingtechie.HIS.enums.Sync_History;
import com.programmingtechie.HIS.mapper.AppointmentMapper;
import com.programmingtechie.HIS.minio.integration.MinioChannel;
import com.programmingtechie.HIS.model.*;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentService {
    final AppointmentRepository appointmentRepository;
    final DoctorRepository doctorRepository;
    final ServiceRepository serviceRepository;
    final RoomRepository roomRepository;
    final HealthCheckResultRepository healthCheckResultRepository;

    final MinioChannel minioChannel;

    final SyncHistoryRepository syncHistoryRepository;

    final AppointmentClient appointmentClient;

    final AppointmentMapper appointmentMapper;

    final HmacUtils hmacUtils;

    final SecretKeys secretKeys;

    public void syncAppointmentsFromAppointmentSystem() {
        String appointmentValue = Sync_History.APPOINTMENT.name();
        SyncHistory syncHistory = syncHistoryRepository.findById(appointmentValue).get();
        log.info(syncHistory.toString());

        LocalDateTime startDate = syncHistory.getDateTime();
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime expiryDateTime = endDate.plusMinutes(10);

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
        try {
            Hmac = hmacUtils.generateHmac(message, secretKeys.getAppointmentSecretKey());
        } catch (Exception e) {
            log.error("Error generating HMAC", e);
        }
        log.info("message: " + message);
        log.info("appointment.secretKey: " + secretKeys.getAppointmentSecretKey());
        log.info("Hmac: " + Hmac);

        PageResponse<AppointmentSyncResponse> pageResponse = null;
        try{
            pageResponse = appointmentClient.getAppointmentsForHIS(startDate, endDate, expiryDateTime, page, size, Hmac);
            saveAppointmentsToHIS(pageResponse.getData());
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
                Hmac = hmacUtils.generateHmac(message, secretKeys.getAppointmentSecretKey());
            } catch (Exception e) {
                log.error("Error generating HMAC for page " + page, e);
            }
            //log.info("message: " + message);
            //log.info("Hmac: " + Hmac);

            // Gửi yêu cầu tiếp theo
            try{
                pageResponse = appointmentClient.getAppointmentsForHIS(startDate, endDate, expiryDateTime, page, size, Hmac);
                saveAppointmentsToHIS(pageResponse.getData());
            }
            catch (Exception e)
            {
                log.error(e.getMessage().toString());
                throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
            }
            totalPages = pageResponse.getTotalPages(); // Cập nhật lại totalPages
        }
        syncHistory.setDateTime(endDate);
        syncHistoryRepository.save(syncHistory);
    }

    private void saveAppointmentsToHIS(List<AppointmentSyncResponse> appointments) {
        // Lưu dữ liệu vào HIS (có thể là gọi một service hoặc repository để lưu vào cơ sở dữ liệu)
        for (AppointmentSyncResponse appointmentSyncResponse : appointments) {
            // Tìm kiếm các đối tượng liên quan từ các repository
            Doctor doctor = doctorRepository.findById(appointmentSyncResponse.getDoctorId()).orElse(null);
            com.programmingtechie.HIS.model.Service service = serviceRepository.findById(appointmentSyncResponse.getServiceId()).orElse(null);
            Room room = roomRepository.findById(appointmentSyncResponse.getRoomId()).orElse(null);

//            // Tạo đối tượng Appointment từ AppointmentSyncResponse
            Appointment appointment = Appointment.builder()
                    .id(appointmentSyncResponse.getId())
                    .dateTime(appointmentSyncResponse.getDateTime()) // Lấy thời gian từ AppointmentSyncResponse
                    .date(appointmentSyncResponse.getDate()) // Lấy ngày từ AppointmentSyncResponse
                    .status(appointmentSyncResponse.getStatus())
                    .orderNumber(appointmentSyncResponse.getOrderNumber())
                    .lastUpdated(appointmentSyncResponse.getLastUpdated())
                    .room(room)
                    .service(service)
                    .doctor(doctor)
                    .doctorServiceId(null)
                    .medicalRecordsId(appointmentSyncResponse.getMedicalRecordsId())
                    .patientsId(appointmentSyncResponse.getPatientsId())
                    .customerId(appointmentSyncResponse.getCustomerId())
                    .replacementDoctorId(appointmentSyncResponse.getReplacementDoctorId())
                    .build();

//            // In log thông tin trước khi lưu
            log.info("Saving appointment: " + appointmentSyncResponse);
//
//            // Lưu đối tượng Appointment vào cơ sở dữ liệu
            appointmentRepository.save(appointment);
        }
    }

    public PageResponse<AppointmentResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Appointment> pageData = appointmentRepository.findAll(pageable);

        List<AppointmentResponse> appointmentResponses = pageData.getContent().stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();

        return PageResponse.<AppointmentResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(appointmentResponses)
                .build();
    }

    public AppointmentResponse getById(String id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if(appointment == null)
            return null;
        return appointmentMapper.toAppointmentResponse(appointment);

    }

    public void syncAppointmentsFromHealthAppointment(LocalDateTime expiryDateTime, String hmac) {
        // Kiểm tra nếu expiryDateTime đã qua thời gian hiện tại
        if (expiryDateTime.isBefore(LocalDateTime.now())) {
            log.error("Request expired! Expiry time: " + expiryDateTime + " Current time: " + LocalDateTime.now());
            throw new IllegalArgumentException("Yêu cầu đã quá hạn!");
        }

        log.info("NHAN YEU CAU DONG BO LICH HEN TU HE THONG DAT LICH");

        List<String> params = new ArrayList<>();
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
        try{
            syncAppointmentsFromAppointmentSystem();
        } catch (Exception e)
        {
            log.info(e.getMessage());
        }
    }
}
