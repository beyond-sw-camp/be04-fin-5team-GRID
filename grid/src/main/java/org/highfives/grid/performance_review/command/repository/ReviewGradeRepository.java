package org.highfives.grid.performance_review.command.repository;


import org.highfives.grid.performance_review.command.aggregate.entity.ReviewGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewGradeRepository extends JpaRepository<ReviewGrade, Integer> {
}
