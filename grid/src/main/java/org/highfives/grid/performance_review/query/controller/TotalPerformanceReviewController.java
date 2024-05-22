package org.highfives.grid.performance_review.query.controller;

import org.highfives.grid.performance_review.query.dto.TotalPerformanceReviewDTO;
import org.highfives.grid.performance_review.query.service.TotalPerformanceReviewService;
import org.highfives.grid.performance_review.query.vo.ResponseDetailTotalVO;
import org.highfives.grid.performance_review.query.vo.ResponseTotalPerformanceReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller(value = "queryTotalPerformanceReviewController")
@RequestMapping("total-performance-review")
public class TotalPerformanceReviewController {

    private final TotalPerformanceReviewService totalPerformanceReviewService;

    @Autowired
    public TotalPerformanceReviewController(TotalPerformanceReviewService totalPerformanceReviewService) {
        this.totalPerformanceReviewService = totalPerformanceReviewService;
    }

    @GetMapping("reviewee/{employeeId}")
    public ResponseEntity<ResponseTotalPerformanceReviewVO> findTotalByRevieweeId(@PathVariable int employeeId){
        List<TotalPerformanceReviewDTO> findTotalList = totalPerformanceReviewService.findTotalByRevieweeId(employeeId);

        ResponseTotalPerformanceReviewVO response =
                ResponseTotalPerformanceReviewVO.builder()
                        .statusCode(200)
                        .message("종합 업적 평가 목록 조회 성공")
                        .findTotalList(findTotalList)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("reviewer/{employeeId}")
    public ResponseEntity<ResponseTotalPerformanceReviewVO> findTotalByReviewerId(@PathVariable int employeeId){
        List<TotalPerformanceReviewDTO> findTotalList = totalPerformanceReviewService.findTotalByReviewerId(employeeId);

        ResponseTotalPerformanceReviewVO response =
                ResponseTotalPerformanceReviewVO.builder()
                        .statusCode(200)
                        .message("종합 업적 평가 목록 조회 성공")
                        .findTotalList(findTotalList)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseTotalPerformanceReviewVO> findAllTotal(){
        List<TotalPerformanceReviewDTO> findTotalList = totalPerformanceReviewService.findAllTotal();

        ResponseTotalPerformanceReviewVO response =
                ResponseTotalPerformanceReviewVO.builder()
                        .statusCode(200)
                        .message("종합 업적 평가 목록 조회 성공")
                        .findTotalList(findTotalList)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDetailTotalVO> findTotalById(@PathVariable int id){
        TotalPerformanceReviewDTO findTotal = totalPerformanceReviewService.findTotalById(id);

        ResponseDetailTotalVO response =
                ResponseDetailTotalVO.builder()
                        .statusCode(200)
                        .message("종합 업적 평가 목록 조회 성공")
                        .findTotal(findTotal)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
