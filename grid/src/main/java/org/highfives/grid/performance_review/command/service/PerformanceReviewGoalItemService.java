package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PerformanceReviewGoalItemService {
    PerformanceReviewGoalItemDTO addGoalItem(PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO);

    PerformanceReviewGoalItemDTO modifyGoalItem(PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO);

    // 목표 항목 삭제
    int removeGoalItem(int id);
}
