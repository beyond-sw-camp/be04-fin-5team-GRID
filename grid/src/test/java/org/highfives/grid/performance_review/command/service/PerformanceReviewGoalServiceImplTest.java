package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReviewGoal;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(value = "commandReviewGoalTest")
class PerformanceReviewGoalServiceImplTest {

    @Autowired
    private PerformanceReviewGoalServiceImpl performanceReviewGoalService;

    @DisplayName("업적 평가 목표 추가")
    @Test
    @Transactional
    public void addGoal(){
        PerformanceReviewGoalDTO performanceReviewGoalDTO = new PerformanceReviewGoalDTO(
                2025,
                "2025 인사평가",
                "IP",
                6,
                "2025-03-01 08:00:00",
                5
        );

        PerformanceReviewGoalDTO saveGoal = performanceReviewGoalService.addNewGoal(performanceReviewGoalDTO);

        assertNotNull(saveGoal);
    }

    @DisplayName("업적 평가 목표 작성 중 상태 변경")
    @Test
    @Transactional
    public void modifyGoalStatusInProgress(){
        int id = 3;
        PerformanceReviewGoalDTO modifyGoalDTO = performanceReviewGoalService.modifyGoalStatusInProgress(id);

        System.out.println(modifyGoalDTO);
        assertNotNull(modifyGoalDTO);
    }


    @DisplayName("업적 평가 목표 상신 상태 작성 중으로 변경 시 예외 발생")
    @Test
    @Transactional
    public void modifyGoalStatusInProgressException(){
        int id = 1;
//        PerformanceReviewGoalDTO modifyGoalDTO = performanceReviewGoalService.modifyGoalStatusInProgress(id);

        assertThrows(RuntimeException.class, () -> {
            performanceReviewGoalService.modifyGoalStatusInProgress(id);
        });
    }
}