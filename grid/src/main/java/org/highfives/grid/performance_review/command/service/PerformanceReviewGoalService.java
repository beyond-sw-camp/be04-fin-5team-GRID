package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;

public interface PerformanceReviewGoalService {

    PerformanceReviewGoalDTO addNewGoal(PerformanceReviewGoalDTO performanceReviewGoalDTO);

    PerformanceReviewGoalDTO modifyGoalStatusInProgress(int id);

    PerformanceReviewGoalDTO modifyGoalStatusSubmit(int id);

    PerformanceReviewGoalDTO modifyGoalStatusRead(int id);

    PerformanceReviewGoalDTO modifyGoalStatusApproval(int id);

    PerformanceReviewGoalDTO modifyGoalStatusDenied(int id);
}
