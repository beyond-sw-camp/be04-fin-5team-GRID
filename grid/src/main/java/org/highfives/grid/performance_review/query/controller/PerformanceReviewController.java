package org.highfives.grid.performance_review.query.controller;

import org.highfives.grid.performance_review.query.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.query.service.PerformanceReviewService;
import org.highfives.grid.performance_review.query.vo.ResponsePerformanceReviewGoalVO;
import org.highfives.grid.performance_review.query.vo.ResponsePerformanceReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "queryPerformanceReviewController")
@RequestMapping("performance-review")
public class PerformanceReviewController {

    private final PerformanceReviewService performanceReviewService;

    @Autowired
    public PerformanceReviewController(PerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    @GetMapping("member/{employeeId}")
    public ResponseEntity<ResponsePerformanceReviewVO> findPerformanceReviewByWriterId(@PathVariable int employeeId){
        List<PerformanceReviewDTO> findReview = performanceReviewService.findPerformanceReviewByWriterId(employeeId);

        ResponsePerformanceReviewVO response =
                new ResponsePerformanceReviewVO().builder()
                        .statusCode(200)
                        .message("평가 목록 조회 성공")
                        .findReviewList(findReview)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
