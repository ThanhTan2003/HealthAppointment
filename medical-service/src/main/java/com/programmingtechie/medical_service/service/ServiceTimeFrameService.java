package com.programmingtechie.medical_service.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import com.programmingtechie.medical_service.APIAuthentication.HmacUtils;
import com.programmingtechie.medical_service.APIAuthentication.SecretKeys;
import com.programmingtechie.medical_service.dto.response.Appointment.ServiceTimeFrameInSyncResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.medical_service.dto.request.AppointmentCountRequest;
import com.programmingtechie.medical_service.dto.request.ServiceTimeFrameRequest;
import com.programmingtechie.medical_service.dto.response.Appointment.ServiceTimeFrameInAppointmentResponse;
import com.programmingtechie.medical_service.dto.response.AppointmentCountResponse;
import com.programmingtechie.medical_service.dto.response.Doctor.DoctorResponse;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.ServiceTimeFrameResponse;
import com.programmingtechie.medical_service.mapper.ServiceTimeFrameMapper;
import com.programmingtechie.medical_service.model.DoctorService;
import com.programmingtechie.medical_service.model.Room;
import com.programmingtechie.medical_service.model.ServiceTimeFrame;
import com.programmingtechie.medical_service.model.TimeFrame;
import com.programmingtechie.medical_service.repository.DoctorServiceRepository;
import com.programmingtechie.medical_service.repository.RoomRepository;
import com.programmingtechie.medical_service.repository.ServiceTimeFrameRepository;
import com.programmingtechie.medical_service.repository.TimeFrameRepository;
import com.programmingtechie.medical_service.repository.httpClient.AppointmentClient;
import com.programmingtechie.medical_service.repository.httpClient.DoctorClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceTimeFrameService {
    private final ServiceTimeFrameRepository serviceTimeFrameRepository;
    private final DoctorServiceRepository doctorServiceRepository;
    private final TimeFrameRepository timeFrameRepository;
    private final RoomRepository roomRepository;

    private final ServiceTimeFrameMapper serviceTimeFrameMapper;

    private final AppointmentClient appointmentClient;
    private final DoctorClient doctorClient;

    final HmacUtils hmacUtils;

    final SecretKeys secretKeys;

    public PageResponse<ServiceTimeFrameResponse> getAllServiceTimeFrames(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ServiceTimeFrame> pageData = serviceTimeFrameRepository.findAll(pageable);

        List<ServiceTimeFrameResponse> serviceTimeFrameResponses = pageData.getContent().stream()
                .map(serviceTimeFrameMapper::toServiceTimeFrameResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceTimeFrameResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceTimeFrameResponses)
                .build();
    }

    public ServiceTimeFrameResponse getServiceTimeFrameById(String id) {
        ServiceTimeFrame serviceTimeFrame = serviceTimeFrameRepository
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Không tìm thấy khung thời gian dịch vụ với id: " + id));
        return serviceTimeFrameMapper.toServiceTimeFrameResponse(serviceTimeFrame);
    }

    @Transactional
    public ServiceTimeFrameResponse createServiceTimeFrame(ServiceTimeFrameRequest serviceTimeFrameRequest) {
        // Kiểm tra sự tồn tại của ServiceTimeFrame với doctorServiceId và dayOfWeek
        List<ServiceTimeFrame> existingTimeFrames = serviceTimeFrameRepository.findByDoctorServiceIdAndDayOfWeek(
                serviceTimeFrameRequest.getDoctorServiceId(),
                serviceTimeFrameRequest.getDayOfWeek(),
                true); // isActive = true

        if (!existingTimeFrames.isEmpty()) {
            // Nếu đã tồn tại và isActive = true, ném lỗi
            throw new IllegalArgumentException("Khung thời gian dịch vụ đã tồn tại!");
        }

        // Kiểm tra xem có ServiceTimeFrame nào tồn tại với doctorServiceId và dayOfWeek mà isActive = false không
        existingTimeFrames = serviceTimeFrameRepository.findByDoctorServiceIdAndDayOfWeek(
                serviceTimeFrameRequest.getDoctorServiceId(),
                serviceTimeFrameRequest.getDayOfWeek(),
                false); // isActive = false

        if (!existingTimeFrames.isEmpty()) {
            // Nếu tồn tại với isActive = false, chỉ cần cập nhật lại trạng thái
            ServiceTimeFrame existingServiceTimeFrame = existingTimeFrames.get(0);
            existingServiceTimeFrame.setIsActive(true);
            existingServiceTimeFrame.setMaximumQuantity(serviceTimeFrameRequest.getMaximumQuantity());
            existingServiceTimeFrame.setStartNumber(serviceTimeFrameRequest.getStartNumber());
            existingServiceTimeFrame.setEndNumber(serviceTimeFrameRequest.getEndNumber());
            existingServiceTimeFrame.setRoom(roomRepository
                    .findById(serviceTimeFrameRequest.getRoomId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Không tìm thấy phòng với id: " + serviceTimeFrameRequest.getRoomId())));
            existingServiceTimeFrame.setTimeFrame(timeFrameRepository
                    .findById(serviceTimeFrameRequest.getTimeFrameId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Không tìm thấy khung thời gian với id: " + serviceTimeFrameRequest.getTimeFrameId())));

            existingServiceTimeFrame = serviceTimeFrameRepository.save(existingServiceTimeFrame);
            return serviceTimeFrameMapper.toServiceTimeFrameResponse(existingServiceTimeFrame);
        }

        // Nếu không tồn tại, tạo mới ServiceTimeFrame
        DoctorService doctorService = doctorServiceRepository
                .findById(serviceTimeFrameRequest.getDoctorServiceId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy dịch vụ bác sĩ với id: " + serviceTimeFrameRequest.getDoctorServiceId()));

        Room room = roomRepository
                .findById(serviceTimeFrameRequest.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy phòng với id: " + serviceTimeFrameRequest.getRoomId()));

        TimeFrame timeFrame = timeFrameRepository
                .findById(serviceTimeFrameRequest.getTimeFrameId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy khung thời gian với id: " + serviceTimeFrameRequest.getTimeFrameId()));

        // Tạo đối tượng ServiceTimeFrame mới
        ServiceTimeFrame serviceTimeFrame = ServiceTimeFrame.builder()
                .dayOfWeek(serviceTimeFrameRequest.getDayOfWeek())
                .maximumQuantity(serviceTimeFrameRequest.getMaximumQuantity())
                .startNumber(serviceTimeFrameRequest.getStartNumber())
                .endNumber(serviceTimeFrameRequest.getEndNumber())
                .doctorService(doctorService)
                .status("Nhận đăng ký")
                .isActive(true)
                .room(room)
                .timeFrame(timeFrame)
                .build();

        serviceTimeFrame = serviceTimeFrameRepository.save(serviceTimeFrame);
        return serviceTimeFrameMapper.toServiceTimeFrameResponse(serviceTimeFrame);
    }

    @Transactional
    public ServiceTimeFrameResponse updateServiceTimeFrame(String id, Map<String, Object> updates) {
        // Tìm kiếm ServiceTimeFrame theo id
        ServiceTimeFrame serviceTimeFrame = serviceTimeFrameRepository
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Không tìm thấy khung thời gian dịch vụ với id: " + id));

        // Duyệt qua các cặp key-value trong updates để cập nhật các trường trong ServiceTimeFrame
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "maximumQuantity":
                    serviceTimeFrame.setMaximumQuantity((Integer) value);
                    break;
                case "startNumber":
                    serviceTimeFrame.setStartNumber((Integer) value);
                    break;
                case "endNumber":
                    serviceTimeFrame.setEndNumber((Integer) value);
                    break;
                case "isActive":
                    serviceTimeFrame.setIsActive((Boolean) value);
                    break;
                case "status":
                    serviceTimeFrame.setStatus((String) value);
                    break;
                case "roomId":
                    Room room = roomRepository
                            .findById((String) value)
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với id: " + value));
                    serviceTimeFrame.setRoom(room);
                    break;
                default:
                    throw new IllegalArgumentException("Trường cập nhật không hợp lệ: " + key);
            }
        }

        // Lưu lại ServiceTimeFrame đã cập nhật
        serviceTimeFrame = serviceTimeFrameRepository.save(serviceTimeFrame);

        // Chuyển đổi thành ServiceTimeFrameResponse và trả về
        return serviceTimeFrameMapper.toServiceTimeFrameResponse(serviceTimeFrame);
    }

    public void deleteServiceTimeFrame(String id) {
        ServiceTimeFrame serviceTimeFrame = serviceTimeFrameRepository
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Không tìm thấy khung thời gian dịch vụ với id: " + id));
        serviceTimeFrameRepository.delete(serviceTimeFrame);
    }

    public List<ServiceTimeFrameResponse> getServiceTimeFramesByDoctorIdAndDayOfWeek(
            String doctorId, String dayOfWeek) {
        List<ServiceTimeFrame> serviceTimeFrames =
                serviceTimeFrameRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);

        return serviceTimeFrames.stream()
                .map(serviceTimeFrameMapper::toServiceTimeFrameResponse)
                .collect(Collectors.toList());
    }

    public PageResponse<String> getDoctorsAllServiceTimeFrames(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<String> doctorIdsPage = serviceTimeFrameRepository.findDistinctDoctorIds(pageable);

        return PageResponse.<String>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorIdsPage.getTotalPages())
                .totalElements(doctorIdsPage.getTotalElements())
                .data(doctorIdsPage.getContent())
                .build();
    }

    public PageResponse<String> getListSpecialtyWithServiceTimeFrames(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<String> doctorIdsPage = serviceTimeFrameRepository.findDistinctSpecialtyIds(pageable);

        return PageResponse.<String>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorIdsPage.getTotalPages())
                .totalElements(doctorIdsPage.getTotalElements())
                .data(doctorIdsPage.getContent())
                .build();
    }

    public List<String> getAvailableDaysForDoctorService(String doctorServiceId) {

        return serviceTimeFrameRepository.findListDayOfWeekByDoctorServiceId(doctorServiceId);
    }

    // Lay danh sach cac khung gio kham
    public List<ServiceTimeFrameResponse> getServiceTimeFramesByDoctorServiceIdAndDayOfWeek(
            String doctorServiceId, String dayOfWeek, Date day) {

        // Tính thời gian một tháng trước để lọc các khung giờ không hoạt động trong thời gian này
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        // Lấy danh sách các khung giờ đang hoạt động (isActive = true)
        List<ServiceTimeFrame> activeServiceTimeFrames =
                serviceTimeFrameRepository.findByDoctorServiceIdAndDayOfWeek(doctorServiceId, dayOfWeek, true);

        // Lấy danh sách các khung giờ không hoạt động trong 1 tháng qua (isActive = false, lastUpdated >= oneMonthAgo)
        List<ServiceTimeFrame> inactiveServiceTimeFrames = serviceTimeFrameRepository.findByDoctorServiceIdAndDayOfWeek(
                doctorServiceId, dayOfWeek, false, oneMonthAgo);

        // Tạo danh sách yêu cầu đếm lịch hẹn cho tất cả các khung giờ (cả khung giờ đang hoạt động và không hoạt động)
        List<AppointmentCountRequest> appointmentCountRequests = new ArrayList<>();

        // Thêm các khung giờ không hoạt động vào danh sách yêu cầu
        for (ServiceTimeFrame inactiveTimeFrame : inactiveServiceTimeFrames) {
            LocalDate appointmentDate =
                    day.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            appointmentCountRequests.add(new AppointmentCountRequest(inactiveTimeFrame.getId(), appointmentDate));
        }

        // Thêm các khung giờ đang hoạt động vào danh sách yêu cầu
        for (ServiceTimeFrame activeTimeFrame : activeServiceTimeFrames) {
            LocalDate appointmentDate =
                    day.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            appointmentCountRequests.add(new AppointmentCountRequest(activeTimeFrame.getId(), appointmentDate));
        }

        // Gọi AppointmentClient để lấy thông tin về tổng số cuộc hẹn cho từng khung giờ
        List<AppointmentCountResponse> appointmentCountResponses =
                appointmentClient.countAppointments(appointmentCountRequests);

        // Tạo một Map để lưu trữ tổng số cuộc hẹn cho mỗi khung giờ
        Map<String, Long> appointmentCountMap = new HashMap<>();
        for (AppointmentCountResponse response : appointmentCountResponses) {
            appointmentCountMap.put(response.getServiceTimeFrameId(), response.getTotal());
        }

        // Lọc ra các khung giờ hợp lệ
        List<ServiceTimeFrameResponse> validServiceTimeFrames = new ArrayList<>();
        for (ServiceTimeFrame activeTimeFrame : activeServiceTimeFrames) {
            long totalAppointments = appointmentCountMap.getOrDefault(activeTimeFrame.getId(), 0L);

            if (totalAppointments < activeTimeFrame.getMaximumQuantity()) {
                ServiceTimeFrameResponse response = serviceTimeFrameMapper.toServiceTimeFrameResponse(activeTimeFrame);
                validServiceTimeFrames.add(response);
            }
        }

        return validServiceTimeFrames;
    }

    // Kiểm tra tồn tại ServiceTimeFrame dựa trên ID, isActive, và status
    public boolean doesServiceTimeFrameExist(String id) {
        return serviceTimeFrameRepository.existsByIdAndIsActiveAndStatus(id);
    }

    String getDayOfWeek(LocalDate date) {
        // Lấy tên ngày trong tuần (2 - 7, CN)
        int dayOfWeek = date.getDayOfWeek().getValue();

        // Chuyển đổi từ 1-7 thành 2-7 và CN
        switch (dayOfWeek) {
            case 7:
                return "CN"; // Chủ nhật
            case 1:
                return "2"; // Thứ 2
            case 2:
                return "3"; // Thứ 3
            case 3:
                return "4"; // Thứ 4
            case 4:
                return "5"; // Thứ 5
            case 5:
                return "6"; // Thứ 6
            case 6:
                return "7"; // Thứ 7
            default:
                throw new IllegalArgumentException("Invalid day of the week");
        }
    }

    public Integer getNextAvailableOrderNumber(
            String serviceTimeFrameId, LocalDate parsedDate, List<Integer> existingOrderNumbers) {
        log.info("Nhan yeu cau...");
        // Lấy ServiceTimeFrame từ repository
        Optional<ServiceTimeFrame> serviceTimeFrame = serviceTimeFrameRepository.findById(serviceTimeFrameId);

        // Kiểm tra khung giờ có tồn tại và hoạt động hay không
        if (serviceTimeFrame.isEmpty() || !serviceTimeFrame.get().getIsActive()) {
            return -1; // Nếu không tồn tại hoặc không hoạt động
        }

        // Kiểm tra ngày đăng ký lịch hẹn có hợp lệ hay không
        if (!Objects.equals(serviceTimeFrame.get().getDayOfWeek(), getDayOfWeek(parsedDate))) {
            log.info("Ngay dung: " + serviceTimeFrame.get().getDayOfWeek());
            log.info("Ngay nhan: " + getDayOfWeek(parsedDate));
            throw new IllegalArgumentException("Ngày đăng ký lịch hẹn không hợp lệ. Vui lòng kiểm tra lại!");
        }

        // Tính thời gian một tháng trước để lọc các khung giờ không hoạt động trong thời gian này
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        // Lấy danh sách các khung giờ không hoạt động trong 1 tháng qua (isActive = false, lastUpdated >= oneMonthAgo)
        List<ServiceTimeFrame> inactiveServiceTimeFrames = serviceTimeFrameRepository.findByDoctorServiceIdAndDayOfWeek(
                serviceTimeFrame.get().getDoctorService().getId(), getDayOfWeek(parsedDate), false, oneMonthAgo);

        // Khởi tạo biến đếm tổng số cuộc hẹn đã có
        Long count = (long) existingOrderNumbers.size(); // Số cuộc hẹn đã có

        // Kiểm tra các khung giờ không hoạt động và thêm số cuộc hẹn vào biến count
        if (!inactiveServiceTimeFrames.isEmpty()) {
            List<AppointmentCountRequest> appointmentCountRequests = new ArrayList<>();

            // Tạo yêu cầu đếm lịch hẹn cho các khung giờ không hoạt động
            for (ServiceTimeFrame inactiveTimeFrame : inactiveServiceTimeFrames) {
                appointmentCountRequests.add(new AppointmentCountRequest(inactiveTimeFrame.getId(), parsedDate));
            }

            try {
                // Gửi yêu cầu đếm cuộc hẹn
                List<AppointmentCountResponse> appointmentCountResponses =
                        appointmentClient.countAppointments(appointmentCountRequests);

                // Cộng dồn số cuộc hẹn đã có từ các khung giờ không hoạt động
                for (AppointmentCountResponse item : appointmentCountResponses) {
                    count += item.getTotal();
                }
            } catch (Exception e) {
                log.error("Lỗi kết nối đến Appointment Service: " + e.getMessage());
                throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại sau!");
            }
        }

        // Kiểm tra nếu số cuộc hẹn đã đạt tối đa
        if (count >= serviceTimeFrame.get().getMaximumQuantity()) {
            return 0; // Nếu đã đủ số lượng, trả về 0
        }

        // Lấy số thứ tự tiếp theo
        Integer startNumber = serviceTimeFrame.get().getStartNumber();
        Integer endNumber = serviceTimeFrame.get().getEndNumber();

        // Duyệt từ startNumber đến endNumber và tìm số thứ tự chưa được sử dụng
        for (Integer orderNumber = startNumber; orderNumber <= endNumber; orderNumber++) {
            if (!existingOrderNumbers.contains(orderNumber)) {
                return orderNumber; // Trả về số thứ tự tiếp theo chưa được sử dụng
            }
        }

        // Nếu không tìm thấy số thứ tự hợp lệ, trả về -1 và thông báo chi tiết
        log.warn("Không còn số thứ tự hợp lệ cho khung giờ: " + serviceTimeFrameId + " vào ngày: " + parsedDate);
        return -1; // Không có số thứ tự hợp lệ
    }

    public List<ServiceTimeFrameInAppointmentResponse> getByIds(LocalDateTime expiryDateTime, String hmac, List<String> ids) {

        // Kiểm tra nếu expiryDateTime đã qua thời gian hiện tại
        if (expiryDateTime.isBefore(LocalDateTime.now())) {
            log.error("Request expired! Expiry time: " + expiryDateTime + " Current time: " + LocalDateTime.now());
            throw new IllegalArgumentException("Yêu cầu đã quá hạn!");
        }

        List<String> params = new ArrayList<>();
        params.add(expiryDateTime.toString());
        params.add(String.valueOf(ids));

        String message = hmacUtils.createMessage(params);

        Boolean isCheck = false;
        try{
            isCheck = hmacUtils.verifyHmac(message, hmac, secretKeys.getMedicalSecretKey());
        }catch (Exception e)
        {
            log.info(e.toString());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
        }
        if(!isCheck)
            throw new IllegalArgumentException("Yêu cầu không hợp lệ!");

        // Lấy danh sách ServiceTimeFrame từ repository
        List<ServiceTimeFrame> serviceTimeFrames = serviceTimeFrameRepository.findAllById(ids);

        // Chuyển đổi ServiceTimeFrame thành ServiceTimeFrameInAppointmentResponse
        List<ServiceTimeFrameInAppointmentResponse> serviceTimeFrameInAppointmentResponses = serviceTimeFrames.stream()
                .map(serviceTimeFrameMapper::toServiceTimeFrameInAppointmentResponse)
                .collect(Collectors.toList());

        // Lấy danh sách doctorIds từ serviceTimeFrameInAppointmentResponses
        List<String> doctorIds = serviceTimeFrameInAppointmentResponses.stream()
                .map(ServiceTimeFrameInAppointmentResponse::getDoctorId)
                .collect(Collectors.toList());

        // Lấy thông tin bác sĩ từ doctorClient
        List<DoctorResponse> doctorResponses = doctorClient.getByIds(doctorIds);

        // Tạo map cho phép tra cứu doctorResponse theo doctorId
        Map<String, DoctorResponse> doctorInfoMap = doctorResponses.stream()
                .collect(Collectors.toMap(DoctorResponse::getId, doctorResponse -> doctorResponse));

        // Cập nhật thông tin bác sĩ cho từng item trong serviceTimeFrameInAppointmentResponses
        serviceTimeFrameInAppointmentResponses.forEach(item -> {
            DoctorResponse doctorResponse = doctorInfoMap.get(item.getDoctorId());
            if (doctorResponse != null) {
                item.setDoctorQualificationName(doctorResponse.getQualificationName());
                item.setDoctorName(doctorResponse.getFullName());
            }
        });

        return serviceTimeFrameInAppointmentResponses;
    }

    public Double getUnitPriceById(String id) {
        ServiceTimeFrame serviceTimeFrame = serviceTimeFrameRepository
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Không tìm thấy khung thời gian dịch vụ với id: " + id));

        return serviceTimeFrame.getDoctorService().getUnitPrice();
    }

    public List<ServiceTimeFrameInSyncResponse> getByIdsPublic(List<String> ids) {
        List<ServiceTimeFrame> serviceTimeFrames = serviceTimeFrameRepository.findAllById(ids);

        return serviceTimeFrames.stream()
                .map(serviceTimeFrameMapper::toServiceTimeFrameInSyncResponse)
                .collect(Collectors.toList());
    }
}
