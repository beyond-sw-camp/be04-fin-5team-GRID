package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReviewGoalItem;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;

import java.util.List;

public interface PerformanceReviewGoalItemService {
    PerformanceReviewGoalItemDTO addGoalItem(org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO);

    PerformanceReviewGoalItemDTO modifyGoalItem(org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO);

    // 목표 항목 삭제
    int removeGoalItem(int id);

    List<PerformanceReviewGoalItemDTO> findByGoalId(int goalId);
}
