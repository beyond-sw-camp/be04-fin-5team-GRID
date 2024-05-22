package org.highfives.grid.performance_review.command.repository;

import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReviewItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceReviewItemRepository extends JpaRepository<PerformanceReviewItem, Integer> {
    List<PerformanceReviewItem> findByReviewId(int id);
}