package org.highfives.grid.performance_review.query.service;

import org.highfives.grid.performance_review.query.dto.PerformanceReviewGoalDTO;

import java.util.List;

public interface PerformanceReviewGoalService {
    List<PerformanceReviewGoalDTO> findAllGoalByWriterId(int employeeId);
}
