package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;

public interface PerformanceReviewGoalService {

    PerformanceReviewGoalDTO addNewGoal(PerformanceReviewGoalDTO performanceReviewGoalDTO);

    PerformanceReviewGoalDTO modifyGoalStatusInProgress(int id);
}
