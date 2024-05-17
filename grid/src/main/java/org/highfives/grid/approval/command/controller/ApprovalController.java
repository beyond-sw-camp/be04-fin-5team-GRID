package org.highfives.grid.approval.command.controller;

import org.highfives.grid.approval.command.service.ApprovalService;
import org.highfives.grid.approval.command.vo.BTApprovalVO;
import org.highfives.grid.approval.command.vo.OvertimeApprovalVO;
import org.highfives.grid.approval.command.vo.RWApprovalVO;
import org.highfives.grid.approval.command.vo.ResApprovalVO;
import org.highfives.grid.approval.common.dto.BTApprovalDTO;
import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.approval.common.dto.RwApprovalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "CommandApprovalController")
@RequestMapping("/approval")
public class ApprovalController {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping("/bt")
    public ResponseEntity<ResApprovalVO> addBTApproval(@RequestBody BTApprovalVO btApprovalVO) {

        BTApprovalDTO result = approvalService.addBTApproval(btApprovalVO);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("출장 결재 생성 성공")
                .href("")
                .btResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/overtime")
    public ResponseEntity<ResApprovalVO> addOvertimeApproval(@RequestBody OvertimeApprovalVO overtimeApprovalVO) {

        OvertimeApprovalDTO result = approvalService.addOvertimeApproval(overtimeApprovalVO);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("시간 외 근무 결재 생성 성공")
                .href("")
                .overtimeResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/rw")
    public ResponseEntity<ResApprovalVO> addRWApproval(@RequestBody RWApprovalVO rwApprovalVO) {

        RwApprovalDTO result = approvalService.addRWApproval(rwApprovalVO);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("단축 근무 결재 생성 성공")
                .href("")
                .rwResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/bt/{btApprovalId}")
    public ResponseEntity<ResApprovalVO> modifyBTApproval(@RequestBody BTApprovalVO btApprovalVO, @PathVariable int btApprovalId) {

        BTApprovalDTO result = approvalService.modifyBTApproval(btApprovalVO, btApprovalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("출장 결재 수정 성공")
                .href("")
                .btResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/overtime/{overtimeApprovalId}")
    public ResponseEntity<ResApprovalVO> modifyOvertimeApproval(@RequestBody OvertimeApprovalVO overtimeApprovalVO, @PathVariable int overtimeApprovalId) {

        OvertimeApprovalDTO result = approvalService.modifyOvertimeApproval(overtimeApprovalVO, overtimeApprovalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("시간 외 결재 수정 성공")
                .href("")
                .overtimeResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/rw/{rwApprovalId}")
    public ResponseEntity<ResApprovalVO> modifyRWApproval(@RequestBody RWApprovalVO rwApprovalVO, @PathVariable int rwApprovalId) {

        RwApprovalDTO result = approvalService.modifyRWApproval(rwApprovalVO, rwApprovalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(200)
                .message("단축 근무 결재 수정 성공")
                .href("")
                .rwResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/bt/{btApprovalId}")
    public ResponseEntity<ResApprovalVO> cancelBTApproval(@PathVariable int btApprovalId) {

        BTApprovalDTO result = approvalService.cancelBTApproval(btApprovalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("출장 결재 취소 결재 생성 성공")
                .href("")
                .btResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/overtime/{overtimeApprovalId}")
    public ResponseEntity<ResApprovalVO> cancelOvertimeApproval(@PathVariable int overtimeApprovalId) {

        OvertimeApprovalDTO result = approvalService.cancelOvertimeApproval(overtimeApprovalId);

        ResApprovalVO response = ResApprovalVO.builder()
                .statusCode(201)
                .message("시간 외 근무 결재 취소 결재 생성 성공")
                .href("")
                .overtimeResult(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
