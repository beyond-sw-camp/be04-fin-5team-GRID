package org.highfives.grid.approval.query.controller;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;

import org.highfives.grid.approval.common.dto.OvertimeInWeekDTO;
import org.highfives.grid.approval.common.vo.ResApprovalVO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;
import org.highfives.grid.approval.query.dto.EmpStatusDTO;
import org.highfives.grid.approval.query.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RestController(value = "QueryApprovalController")
@RequestMapping("/approval")
public class ApprovalController {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/all/{typeId}/{isApproval}")
    public ResponseEntity<ResApprovalVO> findAllBTApproval(@PathVariable int typeId, @PathVariable int isApproval) {

        List<ApprovalEmpDTO> result = approvalService.findAllBTApproval(typeId, isApproval);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("출장 결재 전체 조회 성공")
                .href("")
                .approvalEmpResultList(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/list/{typeId}/{isApproval}/{employeeId}")
    public ResponseEntity<ResApprovalVO> findAllApprovalByEmployeeId(@PathVariable int typeId, @PathVariable int isApproval, @PathVariable int employeeId) {

        List<ApprovalEmpDTO> result = approvalService.findAllApprovalByEmployeeId(typeId, isApproval, employeeId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("작성자별 결재 목록 조회 성공")
                .href("")
                .approvalEmpResultList(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/detail/{typeId}/{approvalId}")
    public ResponseEntity<ResApprovalVO> findDetailByApprovalId(@PathVariable int typeId, @PathVariable int approvalId) {

        ApprovalEmpDTO result = approvalService.findDetailByApprovalId(typeId, approvalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("결재 상세 조회 성공")
                .href("")
                .approvalEmpResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/overtime-count/{employeeId}")
    public ResponseEntity<Double> countOvertimeInWeek(@PathVariable int employeeId) {

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();

        String sunday = now.with(LocalTime.MIN).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).format(dateFormat);
        String saturday = now.with(LocalTime.MAX).with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).format(dateFormat);

        double count = approvalService.countOvertimeInWeek(new OvertimeInWeekDTO(sunday, saturday, employeeId));

        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("/approver/{typeId}/{isApproval}/{employeeId}")
    public ResponseEntity<ResApprovalVO> findAllApprovalByApproverId(@PathVariable int typeId, @PathVariable int isApproval, @PathVariable int employeeId) {

        List<ApprovalEmpDTO> result = approvalService.findAllApprovalByApproverId(typeId, isApproval, employeeId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("결재자별 결재 목록 조회 성공")
                .href("")
                .approvalEmpResultList(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/today/bt")
    public ResponseEntity<ResApprovalVO> findTodayBT() {

        List<ApprovalEmpDTO> result = approvalService.findTodayBT();

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("금일 출장 조회 성공")
                .href("")
                .approvalEmpResultList(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/today/v")
    public ResponseEntity<ResApprovalVO> findTodayV() {

        List<ApprovalEmpDTO> result = approvalService.findTodayV();

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("금일 휴가 조회 성공")
                .href("")
                .approvalEmpResultList(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/today/work")
    public ResponseEntity<ResApprovalVO> findEmpStatus(){

        List<EmpStatusDTO> result = approvalService.findEmpStatus();

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("직원 상태 조회 성공")
                .href("")
                .empStatusResultList(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
