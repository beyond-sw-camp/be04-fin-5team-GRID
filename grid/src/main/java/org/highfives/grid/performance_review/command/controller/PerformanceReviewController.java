package org.highfives.grid.performance_review.command.controller;

import org.highfives.grid.performance_review.command.dto.ModifyPerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewItemDTO;
import org.highfives.grid.performance_review.command.service.PerformanceReviewService;
import org.highfives.grid.performance_review.command.vo.RequestPerformanceReviewVO;
import org.highfives.grid.performance_review.command.vo.ResponseModifyPerformanceReviewVO;
import org.highfives.grid.performance_review.command.vo.ResponsePerformanceReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 업적 평가 저장
    @PutMapping("/in-progress")
    public ResponseEntity<ResponseModifyPerformanceReviewVO> modifyPerformanceReview(
            @RequestBody RequestPerformanceReviewVO requestPerformanceReviewVO){

        System.out.println(requestPerformanceReviewVO);
        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusInProgress(requestPerformanceReviewVO);

        ResponseModifyPerformanceReviewVO response = ResponseModifyPerformanceReviewVO.builder()
                .statusCode(200)
                .message("평가 수정 완료")
                .href("performance-review/detail/{id}")
                .modifyPerformanceReviewDTO(modifyPerformanceReviewDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
