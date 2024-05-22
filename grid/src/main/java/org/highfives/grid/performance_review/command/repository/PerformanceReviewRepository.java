package org.highfives.grid.performance_review.command.repository;

import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Integer> {
}
