package org.highfives.grid.performance_review.command.repository;

import org.highfives.grid.performance_review.command.aggregate.entity.TotalPerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalPerformanceReviewRepository extends JpaRepository<TotalPerformanceReview, Integer> {
}
