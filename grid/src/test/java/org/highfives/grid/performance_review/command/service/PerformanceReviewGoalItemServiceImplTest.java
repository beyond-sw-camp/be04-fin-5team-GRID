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


    @DisplayName("업적 평가 목표 항목 추가")
    @Test

    public void addGoalItem() {
        PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO = new PerformanceReviewGoalItemDTO(
                null,
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

    @DisplayName("업적 평가 목표 항목 수정")
    @Test
    @Transactional
    public void modifyGoalItem() {
        PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO = new PerformanceReviewGoalItemDTO(
                6,
                " 수정 웹 애플리케이션 보안 강화",
                "수정 웹 애플리케이션의 보안 취약점을 보완하기 위한 작업",
                "수정 해결된 보안 취약점 수",
                40,
                "수정 매월 보안 취약점 스캔",
                null,
                4
        );

        System.out.println(performanceReviewGoalItemDTO);

        PerformanceReviewGoalItemDTO modifyGoalItem = performanceReviewGoalItemService.modifyGoalItem(performanceReviewGoalItemDTO);

        assertNotNull(modifyGoalItem);
    }

    @DisplayName("업적 평가 목표 항목 삭제 ")
    @Test
    @Transactional
    public void removeGoalItem() {
        int id = 5;

        int removeId = performanceReviewGoalItemService.removeGoalItem(id);

        assertEquals(5, removeId);
    }
}