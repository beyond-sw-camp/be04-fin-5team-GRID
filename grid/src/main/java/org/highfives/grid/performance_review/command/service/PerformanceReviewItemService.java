package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewItemDTO;

import java.util.List;

public interface PerformanceReviewItemService {
    List<PerformanceReviewItemDTO> addNewItems(int goalId, int reviewId);
    PerformanceReviewItemDTO modifyItem(PerformanceReviewItemDTO performanceReviewItemDTO);

    List<PerformanceReviewItemDTO> findByReviewId(int id);
}
