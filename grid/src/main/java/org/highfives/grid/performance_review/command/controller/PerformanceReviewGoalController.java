package org.highfives.grid.performance_review.command.controller;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.performance_review.command.service.PerformanceReviewGoalService;
import org.highfives.grid.performance_review.command.vo.ResponsePerformanceReviewGoalVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "commandGoalController")
@RequestMapping("review-goal")
public class PerformanceReviewGoalController {

    private final PerformanceReviewGoalService performanceReviewGoalService;
    private final ModelMapper modelMapper;

    @Autowired
    public PerformanceReviewGoalController(PerformanceReviewGoalService performanceReviewGoalService, ModelMapper modelMapper) {
        this.performanceReviewGoalService = performanceReviewGoalService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ResponsePerformanceReviewGoalVO> addGoal(
            @RequestBody PerformanceReviewGoalDTO performanceReviewGoalDTO){

        PerformanceReviewGoalDTO addGoalDTO = performanceReviewGoalService.addGoal(performanceReviewGoalDTO);
        ResponsePerformanceReviewGoalVO response = ResponsePerformanceReviewGoalVO.builder()
                .statusCode(200)
                .message("평가 추가 완료")
                .href("review-goal/detail/{id}")
                .goal(addGoalDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
