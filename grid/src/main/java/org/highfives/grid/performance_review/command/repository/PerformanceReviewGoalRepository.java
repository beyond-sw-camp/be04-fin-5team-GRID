package org.highfives.grid.performance_review.command.repository;

import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReviewGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceReviewGoalRepository extends JpaRepository<PerformanceReviewGoal, Integer> {
    PerformanceReviewGoal findByYearAndWriterId(int year, int writerId);
}
