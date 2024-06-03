package org.highfives.grid.work.query.controller;

import org.highfives.grid.work.common.dto.AdTimeDTO;
import org.highfives.grid.work.query.service.AdTimeService;
import org.highfives.grid.work.query.vo.ResponseAdTimeListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value="QueryAdTimeController")
@RequestMapping("ad-time")
public class AdTimeController {
    private final AdTimeService adTimeService;

    @Autowired
    public AdTimeController(AdTimeService adTimeService) {
        this.adTimeService = adTimeService;
    }

    // 조회 null일 때 예외 처리 필요
    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseAdTimeListVO> findAdTimeByEmployeeId(@PathVariable int employeeId) {
        List<AdTimeDTO> adTimeList = adTimeService.findAdTimeByEmployeeId(employeeId);

        ResponseAdTimeListVO response = ResponseAdTimeListVO.builder()
                .statusCode(200)
                .message("출퇴근 목록 조회 성공")
                .href("")
                .adTimeDTOList(adTimeList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseAdTimeListVO> findAdTimeAll() {
        List<AdTimeDTO> adTimeList = adTimeService.findAdTimeAll();

        ResponseAdTimeListVO response = ResponseAdTimeListVO.builder()
                .statusCode(200)
                .message("출퇴근 목록 조회 성공")
                .href("")
                .adTimeDTOList(adTimeList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/date/{startTime}")
    public ResponseEntity<ResponseAdTimeListVO> findAdTimeByStartTime(
            @PathVariable String startTime) {
        List<AdTimeDTO> adTimeList = adTimeService.findAdTimeByStartTime(startTime);

        ResponseAdTimeListVO response = ResponseAdTimeListVO.builder()
                .statusCode(200)
                .message("출퇴근 목록 조회 성공")
                .href("")
                .adTimeDTOList(adTimeList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
