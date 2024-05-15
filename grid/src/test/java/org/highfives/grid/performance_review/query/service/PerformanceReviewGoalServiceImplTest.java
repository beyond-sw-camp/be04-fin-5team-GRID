package org.highfives.grid.performance_review.query.service;

import org.highfives.grid.performance_review.query.dto.PerformanceReviewGoalDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PerformanceReviewGoalServiceImplTest {

    @Autowired
    private PerformanceReviewGoalServiceImpl performanceReviewGoalService;

    @DisplayName("6번 직원의 업적 평가 목록 조회")
    @Test
    public void findGoalByWriterId(){
        int employeeId = 6;
        List<PerformanceReviewGoalDTO> findGoal = performanceReviewGoalService.findAllGoalByWriterId(employeeId);

        assertNotNull(findGoal);
    }

    @DisplayName("5번 직원이 결재할 업적 평가 목록 조회")
    @Test
    public void findGoalByEApproverId(){
        int employeeId = 5;
        List<PerformanceReviewGoalDTO> findGoal = performanceReviewGoalService.findAllGoalByApproverId(employeeId);

        assertNotNull(findGoal);
    }
}