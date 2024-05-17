package org.highfives.grid.performance_review.query.service;

import org.highfives.grid.performance_review.query.dto.PerformanceReviewDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PerformanceReviewServiceImplTest {

    @Autowired
    private PerformanceReviewServiceImpl performanceReviewService;

    @DisplayName("6번 직원의 작성한 평가항목 조회")
    @Test
    public void findReviewByWriterId() {
        int id = 6;
        List<PerformanceReviewDTO> findReview = performanceReviewService.findPerformanceReviewByWriterId(id);

        assertNotNull(findReview);
    }
}