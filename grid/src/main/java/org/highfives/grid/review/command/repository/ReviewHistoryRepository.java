package org.highfives.grid.review.command.repository;

import org.highfives.grid.review.command.aggregate.ReviewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewHistoryRepository extends JpaRepository<ReviewHistory, Integer> {



}
