package org.highfives.grid.performance_review.query.service;


import org.highfives.grid.performance_review.query.dto.TotalPerformanceReviewDTO;

import java.util.List;

public interface TotalPerformanceReviewService {

    List<TotalPerformanceReviewDTO> findTotalByRevieweeId(int employeeId);
    List<TotalPerformanceReviewDTO> findTotalByReviewerId(int employeeId);
    List<TotalPerformanceReviewDTO> findAllTotal();
    TotalPerformanceReviewDTO findTotalById(int id);
}
