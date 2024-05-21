package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewItemDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PerformanceReviewItemServiceImplTest {

    @Autowired
    private PerformanceReviewItemServiceImpl performanceReviewItemService;

    @DisplayName("평가 항목 생성")
    @Test
    @Transactional
    public void addPerformanceReviewItems(){
        int goalId = 3;
        int reviewId = 5;
        List<PerformanceReviewItemDTO> performanceReviewItemDTOList = performanceReviewItemService.addNewItems(goalId, reviewId);
        System.out.println(performanceReviewItemDTOList);
        assertNotNull(performanceReviewItemDTOList);
    }

}