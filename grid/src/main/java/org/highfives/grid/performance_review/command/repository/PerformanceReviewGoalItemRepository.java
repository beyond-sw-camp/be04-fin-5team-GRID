package org.highfives.grid.performance_review.command.repository;

import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReviewGoalItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceReviewGoalItemRepository extends JpaRepository<PerformanceReviewGoalItem, Integer> {

    List<PerformanceReviewGoalItem> findByGoalId(int goalId);
}
