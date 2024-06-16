package org.highfives.grid.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.highfives.grid.approval.common.vo.ResApprovalVO;
import org.highfives.grid.approval_chain.command.repository.BTApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.OApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.RWApprovalChainRepository;
import org.highfives.grid.approval_chain.command.repository.VApprovalChainRepository;
import org.highfives.grid.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ApprovalAspect {

    @Value("email.id")
    private String fromEmail;

    @Value("email.password")
    private String password;

    private final EmailService emailService;
    private final BTApprovalChainRepository btApprovalChainRepository;
    private final OApprovalChainRepository oApprovalChainRepository;
    private final VApprovalChainRepository vApprovalChainRepository;
    private final RWApprovalChainRepository rwApprovalChainRepository;

    @Autowired
    public ApprovalAspect(EmailService emailService, BTApprovalChainRepository btApprovalChainRepository,
                          OApprovalChainRepository oApprovalChainRepository,
                          VApprovalChainRepository vApprovalChainRepository,
                          RWApprovalChainRepository rwApprovalChainRepository) {
        this.emailService = emailService;
        this.btApprovalChainRepository = btApprovalChainRepository;
        this.oApprovalChainRepository = oApprovalChainRepository;
        this.vApprovalChainRepository = vApprovalChainRepository;
        this.rwApprovalChainRepository = rwApprovalChainRepository;
    }



    @Pointcut("execution(* org.highfives.grid.approval.command.controller.ApprovalController.addBTApproval(..))")
    public void btAlarmPointcut() {

    }

    @AfterReturning(value = "ApprovalAspect.btAlarmPointcut()", returning = "response")
    public void approvalAlarm(JoinPoint joinPoint, ResponseEntity<ResApprovalVO> response) {

        ResApprovalVO res = response.getBody();
        log.info("BT alarm occurred");


//        emailService.sendEmail(fromEmail, password, );

    }

}
