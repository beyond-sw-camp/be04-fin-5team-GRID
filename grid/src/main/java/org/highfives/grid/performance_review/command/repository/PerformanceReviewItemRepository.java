package org.highfives.grid.performance_review.command.repository;

import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReviewItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceReviewItemRepository extends JpaRepository<PerformanceReviewItem, Integer> {
}