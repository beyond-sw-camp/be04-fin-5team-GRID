package org.highfives.grid.work.query.controller;

import org.highfives.grid.work.query.dto.BtApprovalDTO;
import org.highfives.grid.work.query.service.BtApprovalService;
import org.highfives.grid.work.query.vo.ResponseBtListVO;
import org.highfives.grid.work.query.vo.ResponseOvertimeListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value="QueryBtApprovalController")
@RequestMapping("/bt")
public class BtApprovalController {

    private final BtApprovalService btApprovalService;

    @Autowired
    public BtApprovalController(BtApprovalService btApprovalService) {
        this.btApprovalService = btApprovalService;
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseBtListVO> findAllBt() {
        List<BtApprovalDTO> btList = btApprovalService.findAllBt();

        ResponseBtListVO response = new ResponseBtListVO(
                "조회 성공",
                btList
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseBtListVO> findBtByEmployeeId(@PathVariable int employeeId) {
        List<BtApprovalDTO> btList = btApprovalService.findBtByEmployeeId(employeeId);

        ResponseBtListVO response = new ResponseBtListVO(
                "조회 성공",
                btList
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
