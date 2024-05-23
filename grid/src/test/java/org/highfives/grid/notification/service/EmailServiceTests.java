package org.highfives.grid.notification.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTests {

    private final EmailService emailService;

    @Autowired
    EmailServiceTests(EmailService emailService) {
        this.emailService = emailService;
    }

    @DisplayName("비밀번호 찾기 이메일 테스트")
    @Test
    void findPwdTest(){

        emailService.sendEmail("wj8300@gmail.com", "hnqh syhp xibh cohb", "jtw5738@naver.com","[Grid] 비밀번호를 변경해주세요"
        ,"<div style=\"text-align: center; font-family: Arial, sans-serif;\">"
                        + "<div style=\"max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;\">"
                        + "<img src=\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSz-CCkIhl1viV7TNN9ZF0KHs465GQeebZ-8Ukb6PVCrg&s\" alt=\"로고\" style=\"width: 100px; margin-bottom: 20px;\">"
                        + "<h2 style=\"color: #333333;\">신규 비밀번호 설정</h2>"
                        + "<p style=\"color: #666666;\">안녕하세요, 플로우입니다.<br>아래 버튼을 눌러 새 비밀번호를 설정해주세요.</p>"
                        + "<a href=\"http://localhost:5173/find/pwd/result\" style=\"display: inline-block; margin-top: 20px; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #5865F2; border-radius: 5px; text-decoration: none;\">확인</a>"
                        + "</div>"
                        + "</div>");
    }

}