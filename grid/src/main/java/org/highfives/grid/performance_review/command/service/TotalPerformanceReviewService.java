package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.TotalPerformanceReviewDTO;

import java.util.List;

public interface TotalPerformanceReviewService {
    TotalPerformanceReviewDTO addTotalReview(List<PerformanceReviewDTO> performanceReviewDTOList);
}
