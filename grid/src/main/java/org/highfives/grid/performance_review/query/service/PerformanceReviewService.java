package org.highfives.grid.performance_review.query.service;

import org.highfives.grid.performance_review.query.dto.DetailPerformanceReviewDTO;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewDTO;

import java.util.List;

public interface PerformanceReviewService {
    List<PerformanceReviewDTO> findPerformanceReviewByWriterId(int employeeId);

    List<PerformanceReviewDTO> findPerformanceReviewByApproverId(int employeeId);

    DetailPerformanceReviewDTO findDetailPerformanceReviewById(int id);

    PerformanceReviewDTO findMidReviewByWriterIdAndYear(int employeeId, int year);

    PerformanceReviewDTO findFinalReviewByWriterIdAndYear(int employeeId, int year);
}
