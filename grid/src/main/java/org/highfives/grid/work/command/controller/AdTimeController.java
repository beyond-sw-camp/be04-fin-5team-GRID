package org.highfives.grid.work.command.controller;

import org.highfives.grid.work.command.service.AdTimeService;
import org.highfives.grid.work.command.vo.ResponseAdTimeVO;
import org.highfives.grid.work.common.dto.AdTimeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value="CommandAdTimeController")
@RequestMapping("ad-time")
public class AdTimeController {

    private final AdTimeService adTimeService;

    @Autowired
    public AdTimeController(AdTimeService adTimeService) {
        this.adTimeService = adTimeService;
    }

    /* 설명. 출근 체크 기능 */
    @PostMapping("/arrival-time")
    public ResponseEntity<ResponseAdTimeVO> addStartTime(@RequestBody AdTimeDTO adTimeDTO) {
        // 시간타입 보고 시간 변경 필요
        AdTimeDTO result = adTimeService.addStartTime(adTimeDTO);

        ResponseAdTimeVO response = new ResponseAdTimeVO(
                "출근 체크",
                result.getId(),
                result.getStartTime(),
                result.getEndTime(),
                result.getEmployeeId()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /* 설명. 퇴근 체크 기능 */
    @PutMapping("/departure-time")
    public ResponseEntity<ResponseAdTimeVO> addEndTime(@RequestBody AdTimeDTO adTimeDTO) {
        AdTimeDTO result = adTimeService.addEndTime(adTimeDTO);

        ResponseAdTimeVO response = new ResponseAdTimeVO(
                "퇴근 체크",
                result.getId(),
                result.getStartTime(),
                result.getEndTime(),
                result.getEmployeeId()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
