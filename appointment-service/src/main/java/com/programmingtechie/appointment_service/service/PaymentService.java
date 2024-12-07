package com.programmingtechie.appointment_service.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.appointment_service.constant.VNPayIPNResponse;
import com.programmingtechie.appointment_service.constant.VNPayParams;
import com.programmingtechie.appointment_service.dto.request.AppointmentCreateRequest;
import com.programmingtechie.appointment_service.dto.request.PaymentRequest;
import com.programmingtechie.appointment_service.dto.request.VNPay.VNPayIPNRequest;
import com.programmingtechie.appointment_service.dto.response.Payment.PaymentResponse;
import com.programmingtechie.appointment_service.enums.PaymentStatus;
import com.programmingtechie.appointment_service.mapper.AppointmentMapper;
import com.programmingtechie.appointment_service.model.Appointment;
import com.programmingtechie.appointment_service.model.Bill;
import com.programmingtechie.appointment_service.model.Payment;
import com.programmingtechie.appointment_service.repository.AppointmentRepository;
import com.programmingtechie.appointment_service.repository.BillRepository;
import com.programmingtechie.appointment_service.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    private final BillService billService;

    private final AppointmentMapper appointmentMapper;

    public static final String VERSION = "2.1.0";
    public static final String COMMAND = "pay";
    public static final String ORDER_TYPE = "other";
    public static final long DEFAULT_MULTIPLIER = 100L;

    @Value("${payment.vnpay.tmn-code}")
    private String tmnCode;

    @Value("${payment.vnpay.init-payment-url}")
    private String initPaymentPrefixUrl;

    @Value("${payment.vnpay.return-url}")
    private String returnUrlFormat;

    @Value("${payment.vnpay.timeout}")
    private Integer paymentTimeout;

    @Value("${payment.vnpay.secret-key}")
    private String secretKey;

    String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

    public String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        Boolean exists = paymentRepository.existsByIdAndDate(sb.toString(), LocalDate.now());
        if (!exists) return sb.toString();

        log.info("Ma bill: " + sb);
        return getRandomNumber(len);
    }

    private String buildPaymentDetail(String id) {
        return String.format("Thanh toan lich hen kham benh " + id);
    }

    private String buildReturnUrl(String txnRef) {
        return String.format(returnUrlFormat, txnRef);
    }

    private Double getPaymentAmount(PaymentRequest paymentRequest) {
        Double unitPrice = paymentRequest.getUnitPrice();
        Double surcharge = paymentRequest.getSurcharge();
        Double discount = paymentRequest.getDiscount();

        unitPrice = (unitPrice != null) ? unitPrice : 0.0;
        surcharge = (surcharge != null) ? surcharge : 0.0;
        discount = (discount != null) ? discount : 0.0;

        Double totalAmount = unitPrice + surcharge;
        Double paymentAmount = totalAmount - discount;

        return Math.max(paymentAmount, 0.0);
    }

    @Transactional
    public PaymentResponse payment(PaymentRequest paymentRequest, HttpServletRequest httpServletRequest)
            throws UnsupportedEncodingException {
        String ipAddress = getIpAddress(httpServletRequest);

        Double paymentAmount = getPaymentAmount(paymentRequest);

        Bill bill = Bill.builder()
                .id(UUID.randomUUID().toString())
                .orderNumber(paymentRequest.getOrderNumber())
                .dateTime(LocalDateTime.now())
                .expiryDateTime(LocalDateTime.now().plusMinutes(paymentTimeout))
                .unitPrice(paymentRequest.getUnitPrice())
                .surcharge(paymentRequest.getSurcharge())
                .totalAmount(paymentRequest.getTotalAmount())
                .discount(paymentRequest.getDiscount())
                .paymentAmount(paymentAmount)
                .status(PaymentStatus.NOT_STARTED.getValue())
                .customerId(paymentRequest.getCustomerId())
                .patientsId(paymentRequest.getAppointmentRequest().getPatientsId())
                .serviceTimeFrameId(paymentRequest.getAppointmentRequest().getServiceTimeFrameId())
                .date(paymentRequest.getAppointmentRequest().getDate())
                .build();

        billRepository.save(bill);

        String paymentURL = getPaymentURL(bill, httpServletRequest);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentRequest(paymentRequest)
                .billId(bill.getId())
                .ipAddress(ipAddress)
                .VNPayURL(paymentURL)
                .build();
        return paymentResponse;
    }

    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public String getPaymentURL(Bill request, HttpServletRequest httpServletRequest)
            throws UnsupportedEncodingException {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        Map<String, String> params = new HashMap<>();

        params.put(VNPayParams.VERSION, VERSION);
        params.put(VNPayParams.COMMAND, COMMAND);

        params.put(VNPayParams.TMN_CODE, tmnCode);
        params.put(VNPayParams.AMOUNT, String.valueOf((long) (request.getPaymentAmount() * DEFAULT_MULTIPLIER)));
        params.put(VNPayParams.CURRENCY, "VND");

        params.put(VNPayParams.TXN_REF, request.getId());
        params.put(VNPayParams.RETURN_URL, buildReturnUrl(request.getId()));

        params.put(VNPayParams.CREATED_DATE, formatter.format(cld.getTime()));

        cld.add(Calendar.MINUTE, paymentTimeout);

        params.put(VNPayParams.EXPIRE_DATE, formatter.format(cld.getTime()));

        params.put(VNPayParams.IP_ADDRESS, getIpAddress(httpServletRequest));
        params.put(VNPayParams.LOCALE, "vn");

        params.put(VNPayParams.ORDER_INFO, buildPaymentDetail(request.getId()));
        params.put(VNPayParams.ORDER_TYPE, ORDER_TYPE);

        List fieldNames = new ArrayList(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = hmacSHA512(secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = initPaymentPrefixUrl + "?" + queryUrl;

        return paymentUrl;
    }

    public void processIpn(Map<String, String> params) {
        // Ánh xạ params vào VNPayIPNRequest
        VNPayIPNRequest vnPayIpnRequest = new VNPayIPNRequest();
        vnPayIpnRequest.setVnp_TmnCode(params.get(VNPayIPNResponse.TMN_CODE));
        vnPayIpnRequest.setVnp_Amount(params.get(VNPayIPNResponse.AMOUNT));
        vnPayIpnRequest.setVnp_BankCode(params.get(VNPayIPNResponse.BANK_CODE));
        vnPayIpnRequest.setVnp_BankTranNo(params.get(VNPayIPNResponse.BANK_TRAN_NO));
        vnPayIpnRequest.setVnp_CardType(params.get(VNPayIPNResponse.CARD_TYPE));
        vnPayIpnRequest.setVnp_OrderInfo(params.get(VNPayIPNResponse.ORDER_INFO));
        vnPayIpnRequest.setVnp_PayDate(params.get(VNPayIPNResponse.PAY_DATE));
        vnPayIpnRequest.setVnp_ResponseCode(params.get(VNPayIPNResponse.RESPONSE_CODE));
        vnPayIpnRequest.setVnp_TransactionNo(params.get(VNPayIPNResponse.TRANSACTION_NO));
        vnPayIpnRequest.setVnp_TxnRef(params.get(VNPayIPNResponse.TXN_REF));
        vnPayIpnRequest.setVnp_SecureHashType(params.get(VNPayIPNResponse.SECURE_HASH_TYPE));
        vnPayIpnRequest.setVnp_SecureHash(params.get(VNPayIPNResponse.SECURE_HASH));

        var reqSecureHash = params.get(VNPayParams.SECURE_HASH);
        params.remove(VNPayParams.SECURE_HASH);
        params.remove(VNPayParams.SECURE_HASH_TYPE);
        var hashPayload = new StringBuilder();
        var fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        var itr = fieldNames.iterator();
        while (itr.hasNext()) {
            var fieldName = itr.next();
            var fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                // Build hash data
                hashPayload.append(fieldName);
                hashPayload.append("=");
                hashPayload.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                if (itr.hasNext()) {
                    hashPayload.append("&");
                }
            }
        }

        var secureHash = hmacSHA512(secretKey, hashPayload.toString());
        Boolean check = secureHash.equals(reqSecureHash);

        if (!check) throw new IllegalArgumentException("Yêu cầu không hợp lệ");

        Bill bill = billRepository.getById(vnPayIpnRequest.getVnp_TxnRef());

        log.info(vnPayIpnRequest.toString());

        log.info("Trang thai: " + vnPayIpnRequest.getVnp_ResponseCode());

        if (!Objects.equals(vnPayIpnRequest.getVnp_ResponseCode(), "00")) {
            billService.deleteBill(bill.getId());
            log.info("Hoa don " + bill.getId() + " giao dich that bai! Da xoa");
            return;
        }

        Payment payment = Payment.builder()
                .id(bill.getId())
                .unitPrice(bill.getUnitPrice())
                .surcharge(bill.getSurcharge())
                .totalAmount(bill.getTotalAmount())
                .discount(bill.getDiscount())
                .paymentAmount(bill.getPaymentAmount())
                .transactionNo(vnPayIpnRequest.getVnp_TransactionNo())
                .responseCode(vnPayIpnRequest.getVnp_ResponseCode())
                .transactionStatus(bill.getTransactionStatus())
                .build();

        log.info("Tao Payment: " + payment.toString());
        paymentRepository.save(payment);

        AppointmentCreateRequest appointmentCreateRequest = AppointmentCreateRequest.builder()
                .dateTime(bill.getDateTime())
                .date(bill.getDate())
                .orderNumber(bill.getOrderNumber())
                .serviceTimeFrameId(bill.getServiceTimeFrameId())
                .patientsId(bill.getPatientsId())
                .customerId(bill.getCustomerId())
                .payment(payment)
                .build();

        log.info("Tao appointmentCreateRequest: " + appointmentCreateRequest.toString());

        log.info("Gui yeu cau tao appointment moi");

        createAppointmentAfterPayment(appointmentCreateRequest);

        log.info("Xao bill: " + bill.toString());
        billService.deleteBill(bill.getId());
    }

    @Transactional
    public void createAppointmentAfterPayment(AppointmentCreateRequest appointmentCreateRequest) {
        Appointment appointment = appointmentMapper.toAppointmentEntity(appointmentCreateRequest);
        appointmentRepository.save(appointment);
        log.info("Them appointment moi thanh cong: " + appointment.toString());
    }
}
