package org.highfives.grid.work.query.controller;

import org.highfives.grid.work.query.dto.AdTimeDTO;
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

@RestController
@RequestMapping("ad-times")
public class AdTimeController {
    private final AdTimeService adTimeService;

    @Autowired
    public AdTimeController(AdTimeService adTimeService) {
        this.adTimeService = adTimeService;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseAdTimeListVO> findAdTimeByEmployeeId(@PathVariable int employeeId) {
        List<AdTimeDTO> adTimeList = adTimeService.findAdTimeByEmployeeId(employeeId);

        ResponseAdTimeListVO response = new ResponseAdTimeListVO(
                "조회 성공",
                adTimeList
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
