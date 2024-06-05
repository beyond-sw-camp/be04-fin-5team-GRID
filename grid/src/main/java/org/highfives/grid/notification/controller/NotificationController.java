package org.highfives.grid.notification.controller;

import org.highfives.grid.notification.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RequestMapping("/mails")
@RestController
public class NotificationController {
//    @GetMapping("/email")
//    public void emailSender() {
//        EmailService emailService = new EmailService();
//
//        // Gmail에서 '앱 비밀번호'를 설정하고 그 값을 여기에 입력해야 합니다.
//        String fromEmail = "wj8300@gmail.com"; // <- 여기에 실제 Gmail 주소를 입력하세요.
//        String password = "hnqh syhp xibh cohb"; // <- 여기에 Gmail 앱 비밀번호를 입력하세요.
//
//        String toEmail = "wjdxodndjaak@naver.com"; // 받는 사람 이메일
//        String subject = "출근 체크 알림"; // 이메일 제목
//        String body = "출근 표시가 안되어있습니다. 확인해주세요!"; // 이메일 본문
//
//        emailService.sendEmail(fromEmail, password, toEmail, subject, body);
//    }

    @GetMapping("/pwd/{email}")
    public void findPwd(@PathVariable("email") String email) {
        EmailService emailService = new EmailService();

        String fromEmail = "wj8300@gmail.com";
        String password = "hnqh syhp xibh cohb";

        String encodedEmail = Base64.getEncoder().encodeToString(email.getBytes());
        String urlEncodedEmail = URLEncoder.encode(encodedEmail, StandardCharsets.UTF_8);

        String subject = "[Grid] 비밀번호를 변경해주세요";
        String body = "<div style=\"text-align: center; font-family: Arial, sans-serif;\">"
                + "<div style=\"max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;\">"
                + "<img src=\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSz-CCkIhl1viV7TNN9ZF0KHs465GQeebZ-8Ukb6PVCrg&s\" alt=\"로고\" style=\"width: 100px; margin-bottom: 20px;\">"
                + "<h2 style=\"color: #333333;\">신규 비밀번호 설정</h2>"
                + "<p style=\"color: #666666;\">안녕하세요, Grid입니다!<br>아래 버튼을 눌러 새 비밀번호를 설정해주세요!</p>"
                + "<a href=\"http://www.gridhr.site/find/pwd/" + urlEncodedEmail + "/result\" style=\"display: inline-block; margin-top: 20px; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #5865F2; border-radius: 5px; text-decoration: none;\">Reset Password</a>"
                + "</div>"
                + "</div>";

        emailService.sendEmail(fromEmail, password, email, subject, body);
    }
}
