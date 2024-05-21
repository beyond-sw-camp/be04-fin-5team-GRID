package org.highfives.grid.performance_review.command.service;


import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;

public interface PerformanceReviewService {

    PerformanceReviewDTO addNewPerformanceReview(PerformanceReviewDTO performanceReviewDTO);
}
