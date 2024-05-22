package org.highfives.grid.performance_review.query.service;

import org.highfives.grid.performance_review.query.dto.DetailGoalDTO;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewGoalDTO;

import java.util.List;

public interface PerformanceReviewGoalService {
    List<PerformanceReviewGoalDTO> findAllGoalByWriterId(int employeeId);

    List<PerformanceReviewGoalDTO> findAllGoalByApproverId(int employeeId);

    DetailGoalDTO findDetailGoalById(int id);

    PerformanceReviewGoalDTO findGoalByWriterIdAndYear(int writerId, int year);
}
