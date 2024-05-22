package org.highfives.grid.performance_review.query.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TotalPerformanceReviewServiceImplTest {

    @Autowired
    private TotalPerformanceReviewServiceImpl totalPerformanceReviewService;

    @DisplayName("평가대상자 기준으로 조회")
    @Test
    public void findTotalByRevieweeId(){
        int employeeId = 6;

        assertNotNull(
                totalPerformanceReviewService.findTotalByRevieweeId(employeeId)
        );

    }

    @DisplayName("평가자 기준으로 조회")
    @Test
    public void findTotalByReviewerId(){
        int employeeId = 5;

        System.out.println(totalPerformanceReviewService.findTotalByReviewerId(employeeId));
        assertNotNull(
                totalPerformanceReviewService.findTotalByReviewerId(employeeId)
        );

    }
}