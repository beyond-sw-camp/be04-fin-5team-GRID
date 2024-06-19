package org.highfives.grid.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.notification.service.EmailService;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ApprovalAspect {

    @Value("${email.id}")
    private String fromEmail;

    @Value("${email.pwd}")
    private String password;

    private final EmailService emailService;
    private final UserService userService;

    private final String subject = "[Grid] 결재 필요 문서";
    private final String body = "<div style=\"text-align: center; font-family: Arial, sans-serif;\">"
            + "<div style=\"max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px;\">"
            + "<img src=\"https://img.freepik.com/premium-vector/document-icon-logo-vector-design-template_827767-2829.jpg\" alt=\"로고\" style=\"width: 100px; margin-bottom: 20px;\">"
            + "<h2 style=\"color: #333333;\">결재 요청</h2>"
            + "<p style=\"color: #666666;\">신규 결재 요청이 생성되었습니다. <br>아래 버튼을 눌러 결재를 진행해주세요!</p>"
            + "<a href=\"http://www.gridhr.site/" + "\" style=\"display: inline-block; margin-top: 20px; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #5865F2; border-radius: 5px; text-decoration: none;\">Grid Login</a>"
            + "</div>"
            + "</div>";

    @Autowired
    public ApprovalAspect(EmailService emailService, UserService userService) {
        this.userService = userService;
        this.emailService = emailService;
    }



    @Pointcut("execution(* org.highfives.grid.approval.command.service.ApprovalServiceImpl.addBTApproval(..))")
    public void btAlarmPointcut() {}

    //출장 결재가 만들어진 뒤 결재 알람을 보냄
    @AfterReturning(value = "ApprovalAspect.btAlarmPointcut()", returning = "response")
    public void approvalAlarm(JoinPoint joinPoint, BTApprovalDTO response) {

        // id의 팀장/부서장 정보 불러와서 해당 팀장/부서장 메일 조회해오기 -> 조회해온 메일주소 사용해서 메일보내기
        //리퀘스터 id로 해당 신청자의 팀장정보 불러옴
        LeaderInfoDTO leaderInfo = userService.findLeaderInfo(response.getRequesterId());

        log.info("BT alarm occurred");

        emailService.sendEmail(fromEmail, password, leaderInfo.getTeamLeaderEmail(), subject, body);
        emailService.sendEmail(fromEmail, password, leaderInfo.getDepLeaderEmail(), subject, body);
    }

}
