package org.highfives.grid.performance_review.command.controller;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;
import org.highfives.grid.performance_review.command.service.PerformanceReviewGoalItemService;
import org.highfives.grid.performance_review.command.vo.ResponsePerformanceReviewGoalItemVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "commandGoalItemController")
@RequestMapping("goal-item")
public class PerformanceReviewGoalItemController {

    private final PerformanceReviewGoalItemService performanceReviewGoalItemService;
    private final ModelMapper modelMapper;

    @Autowired
    public PerformanceReviewGoalItemController(PerformanceReviewGoalItemService performanceReviewGoalItemService, ModelMapper modelMapper) {
        this.performanceReviewGoalItemService = performanceReviewGoalItemService;
        this.modelMapper = modelMapper;
    }

//    @PostMapping
//    public ResponseEntity<ResponsePerformanceReviewGoalItemVO> addGoalItem(
//            @RequestBody PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO) {
//
//    }
}
