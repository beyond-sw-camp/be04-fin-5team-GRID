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
       try{
           // 시간타입 보고 시간 변경 필요
           AdTimeDTO result = adTimeService.addStartTime(adTimeDTO);

           ResponseAdTimeVO response = ResponseAdTimeVO.builder()
                   .statusCode(200)
                   .message("출근 시간 추가 완료")
                   .href("")
                   .adTimeDTO(result)
                   .build();

           return ResponseEntity.status(HttpStatus.OK).body(response);
       } catch (Exception e){
           ResponseAdTimeVO response = ResponseAdTimeVO.builder()
                   .statusCode(HttpStatus.BAD_REQUEST.value())
                   .message(e.getMessage())
                   .href("")
                   .adTimeDTO(null)
                   .build();

           return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
       }

    }

    /* 설명. 퇴근 체크 기능 */
    @PutMapping("/departure-time")
    public ResponseEntity<ResponseAdTimeVO> addEndTime(@RequestBody AdTimeDTO adTimeDTO) {
        try{
            AdTimeDTO result = adTimeService.addEndTime(adTimeDTO);

            ResponseAdTimeVO response = ResponseAdTimeVO.builder()
                    .statusCode(200)
                    .message("퇴근 시간 추가 완료")
                    .href("")
                    .adTimeDTO(result)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            ResponseAdTimeVO response = ResponseAdTimeVO.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .href("")
                    .adTimeDTO(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
        }
    }

    @GetMapping("/{date}/{employeeId}")
    public ResponseEntity<ResponseAdTimeVO> findAdTime(@PathVariable String date, @PathVariable int employeeId){
        try{
            AdTimeDTO result = adTimeService.findAdTime(date, employeeId);

            ResponseAdTimeVO response = ResponseAdTimeVO.builder()
                    .statusCode(200)
                    .message("오늘 출퇴근 조회")
                    .href("")
                    .adTimeDTO(result)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            ResponseAdTimeVO response = ResponseAdTimeVO.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .href("")
                    .adTimeDTO(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
        }
    }

}
