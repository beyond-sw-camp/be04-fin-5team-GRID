package org.highfives.grid.approval.query.controller;

import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.vo.ResApprovalVO;
import org.highfives.grid.approval.query.dto.ApprovalEmpDTO;
import org.highfives.grid.approval.query.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "QueryApprovalController")
@RequestMapping("/approval")
public class ApprovalController {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/bt-all")
    public ResponseEntity<ResApprovalVO> findAllBTApproval() {

        List<BTApprovalDTO> result = approvalService.findAllBTApproval();

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("출장 결재 전체 조회 성공")
                .href("")
                .btResultList(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/list/{typeId}/{employeeId}")
    public ResponseEntity<ResApprovalVO> findAllBTApprovalByEmployeeId(@PathVariable int typeId, @PathVariable int employeeId) {

        ApprovalEmpDTO result = new ApprovalEmpDTO();
        String type = new String();

        switch (typeId) {
            case 1:
                type = "출장";
                result = approvalService.findAllBTApprovalByEmployeeId(1, employeeId);
                break;

            case 2:
                type = "시간 외 근무";

        }

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("직원별" + type + " 결재 조회 성공")
                .href("")
                .approvalEmpResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
