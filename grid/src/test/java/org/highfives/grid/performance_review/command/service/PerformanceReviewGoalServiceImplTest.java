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

    @DisplayName("업적 평가 목표 상태 작성 중으로 변경")
    @Test
    @Transactional
    public void modifyGoalStatusInProgress(){
        int id = 3;
        PerformanceReviewGoalDTO modifyGoalDTO = performanceReviewGoalService.modifyGoalStatusInProgress(id);

        System.out.println(modifyGoalDTO);
        assertNotNull(modifyGoalDTO);
    }


    @DisplayName("업적 평가 목표 상신 상태의 경우 작성 중으로 변경 시 예외 발생")
    @Test
    @Transactional
    public void modifyGoalStatusInProgressException(){
        int id = 1;

        assertThrows(RuntimeException.class, () -> {
            performanceReviewGoalService.modifyGoalStatusInProgress(id);
        });
    }

    @DisplayName("업적 평가 목표 상태 상신으로 변경")
    @Test
    @Transactional
    public void modifyGoalStatusSubmit(){
        int id = 4;
        PerformanceReviewGoalDTO modifyGoalDTO = performanceReviewGoalService.modifyGoalStatusSubmit(id);

        System.out.println(modifyGoalDTO);
        assertNotNull(modifyGoalDTO);
    }

    @DisplayName("업적 평가 목표 상태 확인 중으로 변경")
    @Test
    @Transactional
    public void modifyGoalStatusRead(){
        int id = 2;
        PerformanceReviewGoalDTO modifyGoalDTO = performanceReviewGoalService.modifyGoalStatusRead(id);

        System.out.println(modifyGoalDTO);
        assertNotNull(modifyGoalDTO);
    }

    @DisplayName("업적 평가 목표 상태 승인으로 변경")
    @Test
    @Transactional
    public void modifyGoalStatusApproval(){
        int id = 2;
        PerformanceReviewGoalDTO modifyGoalDTO = performanceReviewGoalService.modifyGoalStatusApproval(id);

        System.out.println(modifyGoalDTO);
        assertNotNull(modifyGoalDTO);
    }

    @DisplayName("업적 평가 목표 상태 반려로 변경")
    @Test
    @Transactional
    public void modifyGoalStatusDenied(){
        int id = 2;
        PerformanceReviewGoalDTO modifyGoalDTO = performanceReviewGoalService.modifyGoalStatusDenied(id);

        System.out.println(modifyGoalDTO);
        assertNotNull(modifyGoalDTO);
    }
}