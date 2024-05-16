package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PerformanceReviewGoalItemServiceImplTest {

    @Autowired
    private PerformanceReviewGoalItemServiceImpl performanceReviewGoalItemService;


    @DisplayName("업적 평가 목표 추가")
    @Test
    @Transactional
    public void addGoalItem() {
        PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO = new PerformanceReviewGoalItemDTO(
                "웹 애플리케이션 보안 강화",
                "웹 애플리케이션의 보안 취약점을 보완하기 위한 작업",
                "해결된 보안 취약점 수",
                40,
                "매월 보안 취약점 스캔",
                null,
                4
        );

        System.out.println(performanceReviewGoalItemDTO);

        PerformanceReviewGoalItemDTO addGoalItem = performanceReviewGoalItemService.addGoalItem(performanceReviewGoalItemDTO);

        assertNotNull(addGoalItem);
    }
}