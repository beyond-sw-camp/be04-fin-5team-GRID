package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;

import java.util.List;

public interface PerformanceReviewGoalItemService {
    PerformanceReviewGoalItemDTO addGoalItem(PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO);

    PerformanceReviewGoalItemDTO modifyGoalItem(PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO);
}
