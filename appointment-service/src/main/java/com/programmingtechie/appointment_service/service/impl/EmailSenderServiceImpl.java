package com.programmingtechie.appointment_service.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.programmingtechie.appointment_service.dto.request.SendEmail.AppointmentBookingInfo;
import com.programmingtechie.appointment_service.dto.request.SendEmail.AppointmentConfirmation;
import com.programmingtechie.appointment_service.dto.request.SendEmail.DoctorReplacementNotification;
import com.programmingtechie.appointment_service.service.EmailSenderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender mailSender;

    String name = "Trần Thanh Tân";
    String email = "tan021101021@tgu.edu.vn";

    @Override
    public void sendEmail(String to, String subject, String message) {
        // Phương thức gửi email văn bản đơn giản
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom("Phòng Khám Đa Khoa Gia Định Tp.Mỹ Tho <phongkhamdakhoatructuyen@gmail.com>");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, true); // "true" để gửi nội dung dưới dạng HTML
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmailAppointmentBookingInfo(AppointmentBookingInfo appointmentBookingInfo) {
        try {
            // Tạo nội dung HTML trực tiếp thay vì đọc từ file
            String emailContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px; font-size: 16px'>"
                    + "<div style='max-width: 600px; margin: 0 auto; background-color: white; border: 1px solid #e0e0e0; border-radius: 10px; overflow: hidden;'>"
                    + "<div style='background-color: #3e8ed0; padding: 20px; color: white; text-align: center;'>"
                    + "<h1 style='margin: 0;'>Thông tin đặt lịch hẹn</h1>"
                    + "</div>"
                    + "<div style='padding: 20px;'>"
                    + "<p>Xin chào!</p>"
                    + "<p style='text-align: justify'>Bạn đã đặt lịch hẹn khám bệnh thành công trên hệ thống đặt lịch khám chữa bệnh trực tuyến của <b>Phòng Khám Đa Khoa Gia Định TP.Mỹ Tho</b>.</p>"
                    + "<p>Dưới đây là thông tin chi tiết:</p>"
                    + "<div style=\"text-align: center;\">"
                    + "<img src=\"https://barcode.tec-it.com/barcode.ashx?data="
                    + appointmentBookingInfo.getId()
                    + "&code=Code128&dpi=96\" alt=\"Mã vạch lịch hẹn\" style=\"max-width: 70%; height: auto; margin-bottom: 20px; text-align: center;\" />"
                    + "</div>"
                    + "<table style='width: 100%; border-collapse: collapse; table-layout: fixed;'>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Mã lịch hẹn:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getId() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Họ tên người khám bệnh:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getPatientName() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Giới tính:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getGender() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Số thẻ bảo hiểm y tế:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getInsuranceNumber() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Số điện thoại:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getPhoneNumber() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Dịch vụ đặt khám:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getServiceName() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Thời gian đặt:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getDateTime() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Ngày khám:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getDate() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Khung giờ khám:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getTimeFrame() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Thông tin bác sĩ:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getDoctorName() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Số tiền thanh toán:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getPaymentAmount() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Trạng thái thanh toán:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                    + appointmentBookingInfo.getPaymentStatus() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Trạng thái lịch hẹn:</strong></td>"
                    + "<td style='padding: 8px; border: 1px solid #e0e0e0;'><strong style='color: #f39c12;'>Chờ phê duyệt</strong></td></tr>"
                    + "</table>"
                    + "<p style='margin-top: 20px; color: #333; text-align: justify'>"
                    + "Lịch hẹn sẽ có hiệu lực khi được xác nhận từ phía phòng khám và sẽ gửi thông tin đến bạn trong thời gian sớm nhất.</p>"
                    + "<p style='margin-top: 20px; color: #333; text-align: justify'>Bạn có thể tra cứu trạng thái hồ sơ tại website đăng ký trực tuyến hoặc trên ứng dụng đi dộng đặt lịch khám chữa bệnh của <b>Phòng Khám Đa Khoa Gia Định TP.Mỹ Tho</b>!</p>"
                    + "<p style='margin-top: 20px; color: #333;'>Trân trọng!</p>"
                    + "<hr /><div style='margin-top: 20px; text-align: justify; color: #173358'>"
                    + "<p><b>PHÒNG KHÁM ĐA KHOA GIA ĐỊNH TP.MỸ THO</b></p>"
                    + "<p><b>Địa chỉ</b>: 102 Hùng Vương, phường 1, thành phố Mỹ Tho, tỉnh Tiền Giang</p>"
                    + "<p><b>Điện thoại</b>: 0256 345 989</p>"
                    + "<p><b>Email</b>: phongkhamgiadinh@gmail.com</p>"
                    + "</div></body></html>";

            // Gửi email
            this.sendEmail(
                    appointmentBookingInfo.getEmailAddress(), "Thông tin đặt lịch khám chữa bệnh", emailContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailAppointmentConfirmation(AppointmentConfirmation appointmentConfirmation) {
        try {
            // Template for the confirmation email
            String emailContent =
                    "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px; font-size: 16px;'>"
                            + "<div style='max-width: 600px; margin: 0 auto; background-color: white; border: 1px solid #e0e0e0; border-radius: 10px; overflow: hidden;'>"
                            + "<div style='background-color: #3e8ed0; padding: 20px; color: white; text-align: center;'>"
                            + "<h1 style='margin: 0;'>Lịch hẹn đã được xác nhận</h1></div>"
                            + "<div style='padding: 20px;'>"
                            + "<p>Xin chào!</p>"
                            + "<p style='text-align: justify'>Lịch hẹn của bạn hiện đã được <b>Phòng Khám Đa Khoa Gia Định TP.Mỹ Tho</b> xác nhận.</p>"
                            + "<p>Dưới đây là thông tin chi tiết:</p>"
                            + "<div style=\"text-align: center;\">"
                            + "<img src=\"https://barcode.tec-it.com/barcode.ashx?data="
                            + appointmentConfirmation.getId()
                            + "&code=Code128&dpi=96\" alt=\"Mã vạch lịch hẹn\" style=\"max-width: 70%; height: auto; margin-bottom: 20px; text-align: center;\" />"
                            + "</div>"
                            + "<table style='width: 100%; border-collapse: collapse; table-layout: fixed;'>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Mã hồ sơ:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + appointmentConfirmation.getId()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Họ tên người khám bệnh:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                            + appointmentConfirmation.getPatientName() + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Giới tính:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                            + appointmentConfirmation.getGender() + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Số bảo hiểm y tế:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                            + appointmentConfirmation.getInsuranceNumber() + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Số điện thoại:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                            + appointmentConfirmation.getPhoneNumber() + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Dịch vụ đặt khám:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                            + appointmentConfirmation.getServiceName() + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Ngày khám:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                            + appointmentConfirmation.getDate() + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Khung giờ khám:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                            + appointmentConfirmation.getTimeFrame() + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Thông tin bác sĩ:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                            + appointmentConfirmation.getDoctorName() + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Phòng khám:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'><strong style='color: #312697;'>"
                            + appointmentConfirmation.getRoomName() + "</strong></td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Số thứ tự:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'><strong style='color: #312697;'>"
                            + appointmentConfirmation.getOrderNumber() + "</strong></td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Trạng thái lịch hẹn:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'><strong style='color: #22b954;'>Đã xác nhận</strong></td></tr>"
                            + "</table>"
                            + "<p style='margin-top: 20px; color: #333; text-align: justify'>Khi đến phòng khám, vui lòng mang theo <strong>thẻ căn cước, thẻ bảo hiểm y tế</strong> và các giấy tờ liên quan khác.</p>"
                            + "<p style='margin-top: 20px; color: #333; text-align: justify'>Lịch hẹn có giá trị áp dụng đến <strong>16 giờ</strong> cùng ngày.</p>"
                            + "<p style='margin-top: 20px; color: #333; text-align: justify'>Bạn có thể tra cứu trạng thái hồ sơ tại website đăng ký trực tuyến hoặc trên ứng dụng đi dộng đặt lịch khám chữa bệnh của <b>Phòng Khám Đa Khoa Gia Định TP.Mỹ Tho</b>!</p>"
                            + "<p style='margin-top: 20px; color: #333; text-align: justify'>Trân trọng!</p>"
                            + "<hr /><div style='margin-top: 20px; text-align: justify; color: #173358'>"
                            + "<p><b>PHÒNG KHÁM ĐA KHOA GIA ĐỊNH TP.MỸ THO</b></p>"
                            + "<p><b>Địa chỉ</b>: 102 Hùng Vương, phường 1, thành phố Mỹ Tho, tỉnh Tiền Giang</p>"
                            + "<p><b>Điện thoại</b>: 0256 345 989</p>"
                            + "<p><b>Email</b>: phongkhamgiadinh@gmail.com</p></div></body></html>";

            // Send the email
            this.sendEmail(appointmentConfirmation.getEmailAddress(), "Xác nhận thông tin lịch hẹn", emailContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendDoctorReplacementNotification(DoctorReplacementNotification notification) {
        try {
            // HTML template for the doctor replacement notification email
            String emailContent =
                    "<html><body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px; font-size: 16px;'>"
                            + "<div style='max-width: 600px; margin: 0 auto; background-color: white; border: 1px solid #e0e0e0; border-radius: 10px; overflow: hidden;'>"
                            + "<div style='background-color: #3e8ed0; padding: 20px; color: white; text-align: center;'>"
                            + "<h1 style='margin: 0;'>Thông báo thay đổi bác sĩ</h1></div>"
                            + "<div style='padding: 20px;'>"
                            + "<p>Xin chào!</p>"
                            + "<p style='text-align: justify;'>Bạn đã đặt lịch hẹn khám chữa bệnh tại <b>Phòng Khám Đa Khoa Gia Định TP. Mỹ Tho</b>.</p>"
                            + "<p>Thông tin như sau:</p>"
                            + "<div style=\"text-align: center;\">"
                            + "<img src=\"https://barcode.tec-it.com/barcode.ashx?data=" + notification.getId()
                            + "&code=Code128&dpi=96\" alt=\"Mã vạch lịch hẹn\" style=\"max-width: 70%; height: auto; margin-bottom: 20px; text-align: center;\" />"
                            + "</div>"
                            + "<table style='width: 100%; border-collapse: collapse; table-layout: fixed;'>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Mã hồ sơ:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + notification.getId()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Họ tên người khám bệnh:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + notification.getPatientName()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Giới tính:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + notification.getGender()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Số bảo hiểm y tế:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>"
                            + notification.getInsuranceNumber() + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Số điện thoại:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + notification.getPhoneNumber()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Dịch vụ đặt khám:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + notification.getServiceName()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Ngày khám:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + notification.getDate()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Khung giờ khám:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + notification.getTimeFrame()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Thông tin bác sĩ:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + notification.getDoctorName()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Phòng khám:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + notification.getRoomName()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Số thứ tự:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'>" + notification.getOrderNumber()
                            + "</td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Trạng thái lịch hẹn:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'><strong style='color: #22b954;'>Đã xác nhận</strong></td></tr></table>"
                            + "<p style='text-align: justify;'>Tuy nhiên, Bác sĩ <strong>"
                            + notification.getDoctorName()
                            + "</strong> sẽ không thể tham gia khám bệnh vào ngày <strong>" + notification.getDate()
                            + "</strong>.</p>"
                            + "<p style='text-align: justify;'>Phòng khám đã sắp xếp bác sĩ mới sẽ thực hiện khám bệnh cho bạn như sau:</p>"
                            + "<table style='width: 100%; border-collapse: collapse; table-layout: fixed;'>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Bác sĩ thay thế:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'><strong style='color: #312697;'>"
                            + notification.getNewDoctorName() + "</strong></td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Phòng khám:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'><strong style='color: #312697;'>"
                            + notification.getNewRoomName() + "</strong></td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Khung giờ khám:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'><strong style='color: #312697;'>"
                            + notification.getNewTimeFrame() + "</strong></td></tr>"
                            + "<tr><td style='padding: 8px; border: 1px solid #e0e0e0;'><strong>Số thứ tự:</strong></td>"
                            + "<td style='padding: 8px; border: 1px solid #e0e0e0;'><strong style='color: #312697;'>"
                            + notification.getNewOrderNumber() + "</strong></td></tr></table>"
                            + "<p style='margin-top: 20px; text-align: justify;'>Nếu bạn có bất kỳ thắc mắc nào, xin vui lòng liên hệ với phòng khám qua hotline: <b>18005094</b>.</p>"
                            + "<p style='margin-top: 20px; color: #333; text-align: justify;'>Khi đến phòng khám, vui lòng mang theo <strong>thẻ căn cước, thẻ bảo hiểm y tế</strong> và các giấy tờ liên quan khác.</p>"
                            + "<p style='margin-top: 20px; color: #333; text-align: justify;'>Bạn có thể tra cứu trạng thái hồ sơ tại website đăng ký trực tuyến hoặc trên ứng dụng di động đặt lịch khám chữa bệnh của <b>Phòng Khám Đa Khoa Gia Định TP. Mỹ Tho</b>!</p>"
                            + "<p style='margin-top: 20px; color: #333;'>Trân trọng!</p>"
                            + "<hr /><div style='margin-top: 20px; text-align: justify; color: #173358'><p><b>PHÒNG KHÁM ĐA KHOA GIA ĐỊNH TP.MỸ THO</b></p>"
                            + "<p><b>Địa chỉ</b>: 102 Hùng Vương, phường 1, thành phố Mỹ Tho, tỉnh Tiền Giang</p>"
                            + "<p><b>Điện thoại</b>: 0256 345 989</p><p><b>Email</b>: phongkhamgiadinh@gmail.com</p></div></body></html>";

            // Send the email
            this.sendEmail(notification.getEmailAddress(), "Thông báo thay đổi bác sĩ", emailContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AppointmentBookingInfo getAppointmentBookingInfo_Sample() {
        return AppointmentBookingInfo.builder()
                .id("LH-2242811085COCQK9AC7L")
                .patientName(name)
                .gender("Nam")
                .insuranceNumber("HS54852********")
                .phoneNumber("0869780998")
                .serviceName("Khám sức khỏe tổng quát")
                .dateTime("2/10/2024 10:24:23")
                .date("15/10/2024")
                .timeFrame("8:00 - 9:00")
                .doctorName("ThS.BS. Nguyễn Đình Tuấn")
                .paymentAmount("150,000")
                .paymentStatus("Đã thanh toán")
                .status("Chờ xác nhận")
                .emailAddress(email)
                .build();
    }

    public AppointmentConfirmation getAppointmentConfirmation_Sample() {
        return AppointmentConfirmation.builder()
                .id("LH-2242811085COCQK9AC7L")
                .patientName(name)
                .gender("Nam")
                .insuranceNumber("HS54852********")
                .phoneNumber("0869780998")
                .serviceName("Khám sức khỏe tổng quát")
                .date("15/10/2024")
                .timeFrame("8:00 - 9:00")
                .doctorName("ThS. BS. Nguyễn Đình Tuấn")
                .roomName("Phòng khám B101")
                .orderNumber("12")
                .appointmentStatus("Đã xác nhận")
                .emailAddress(email)
                .build();
    }

    public DoctorReplacementNotification getDoctorReplacementNotification_Sample() {
        return DoctorReplacementNotification.builder()
                .id("LH-2242811085COCQK9AC7L")
                .patientName(name)
                .gender("Nam")
                .insuranceNumber("HS54852********")
                .phoneNumber("0869780998")
                .serviceName("Khám sức khỏe tổng quát")
                .date("15/10/2024")
                .timeFrame("8:00 - 9:00")
                .doctorName("ThS.BS. Nguyễn Đình Tuấn")
                .roomName("Phòng khám B102")
                .orderNumber("12")
                .status("Đã xác nhận")
                .newDoctorName("TS. BS. Nguyễn Văn Tâm")
                .NewRoomName("Phòng khám B102")
                .newTimeFrame("8:00 - 9:00")
                .newOrderNumber("12")
                .emailAddress(email)
                .build();
    }

    public void sendEmailAppointmentConfirmation() {
        AppointmentConfirmation appointmentConfirmation = getAppointmentConfirmation_Sample();
        sendEmailAppointmentConfirmation(appointmentConfirmation);
    }

    public void sendEmailAppointmentBookingInfo() {
        AppointmentBookingInfo appointmentBookingInfo = getAppointmentBookingInfo_Sample();
        sendEmailAppointmentBookingInfo(appointmentBookingInfo);
    }

    public void sendDoctorReplacementNotification() {
        DoctorReplacementNotification notification = getDoctorReplacementNotification_Sample();
        sendDoctorReplacementNotification(notification);
    }
}
