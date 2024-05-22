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
        ,"<a href=\"http://localhost:5173/find/pwd/result\"><img src=\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSz-CCkIhl1viV7TNN9ZF0KHs465GQeebZ-8Ukb6PVCrg&s\" alt=\"로고\"></a>\n");
    }

}