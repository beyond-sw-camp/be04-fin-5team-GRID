package org.highfives.grid.performance_review.command.repository;

import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReview;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Integer> {
    List<PerformanceReview> findByGoalId(int goalId);
}
