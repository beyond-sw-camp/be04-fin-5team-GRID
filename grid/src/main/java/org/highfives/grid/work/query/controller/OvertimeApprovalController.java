package org.highfives.grid.work.query.controller;

import org.highfives.grid.approval.common.dto.OvertimeApprovalDTO;
import org.highfives.grid.work.query.service.OvertimeApprovalService;
import org.highfives.grid.work.query.vo.ResponseOvertimeListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value="QueryOvertimeApprovalController")
@RequestMapping("/overtimes")
public class OvertimeApprovalController {

    private final OvertimeApprovalService overtimeApprovalService;

    @Autowired
    public OvertimeApprovalController(OvertimeApprovalService overtimeApprovalService) {
        this.overtimeApprovalService = overtimeApprovalService;
    }

    /* 설명. 승인된 시간 외 근무 전체 조회 */
    @GetMapping("/all")
    public ResponseEntity<ResponseOvertimeListVO> findAllOvertime() {
        List<OvertimeApprovalDTO> overtimeList = overtimeApprovalService.findOvertimeAll();

        ResponseOvertimeListVO response = new ResponseOvertimeListVO(
                "조회 성공",
                overtimeList
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 설명. 승인된 시간 외 근무 직원별 조회 */
    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseOvertimeListVO> findAllOvertime(@PathVariable int employeeId) {
        List<OvertimeApprovalDTO> overtimeList = overtimeApprovalService.findOvertimeByEmployeeId(employeeId);

        ResponseOvertimeListVO response = new ResponseOvertimeListVO(
                "조회 성공",
                overtimeList
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
