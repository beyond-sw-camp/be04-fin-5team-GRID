package org.highfives.grid.notification.controller;

import org.highfives.grid.notification.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notification")
@RestController
public class NotificationController {
    @GetMapping("/email")
    public void emailSender() {
        EmailService emailService = new EmailService();

        // Gmail에서 '앱 비밀번호'를 설정하고 그 값을 여기에 입력해야 합니다.
        String fromEmail = "wj8300@gmail.com"; // <- 여기에 실제 Gmail 주소를 입력하세요.
        String password = "hnqh syhp xibh cohb"; // <- 여기에 Gmail 앱 비밀번호를 입력하세요.

        String toEmail = "wjdxodndjaak@naver.com"; // 받는 사람 이메일
        String subject = "출근 체크 알림"; // 이메일 제목
        String body = "출근 표시가 안되어있습니다. 확인해주세요!"; // 이메일 본문

        emailService.sendEmail(fromEmail, password, toEmail, subject, body);
    }
}
