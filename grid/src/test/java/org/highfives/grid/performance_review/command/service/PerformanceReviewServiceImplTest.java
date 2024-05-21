package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PerformanceReviewServiceImplTest {

    @Autowired
    private PerformanceReviewServiceImpl performanceReviewService;

    @DisplayName("업적 평가 생성")
    @Test
    @Transactional
    public void addNewPerformanceReview(){
        PerformanceReviewDTO performanceReviewDTO = new PerformanceReviewDTO(
                "M",
                2024,
                "2024 인사평가",
                8
        );

        PerformanceReviewDTO addReview = performanceReviewService.addNewPerformanceReview(performanceReviewDTO);

        System.out.println(addReview);
        assertNotNull(addReview);
    }

}