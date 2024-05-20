package org.highfives.grid.performance_review.command.controller;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.command.service.PerformanceReviewService;
import org.highfives.grid.performance_review.command.vo.ResponsePerformanceReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "commandPerformanceReviewController")
@RequestMapping("performance-review")
public class PerformanceReviewController {

    private final PerformanceReviewService performanceReviewService;

    @Autowired
    public PerformanceReviewController(PerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    // 업적 평가 처음 생성
    @PostMapping
    public ResponseEntity<ResponsePerformanceReviewVO> addPerformanceReview(
            @RequestBody PerformanceReviewDTO performanceReviewDTO){

        PerformanceReviewDTO addPerformanceReviewDTO = performanceReviewService.addNewPerformanceReview(performanceReviewDTO);

        ResponsePerformanceReviewVO response = ResponsePerformanceReviewVO.builder()
                .statusCode(200)
                .message("평가 추가 완료")
                .href("performance-review/detail/{id}")
                .performanceReview(addPerformanceReviewDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
