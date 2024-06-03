package org.highfives.grid.review.command.repository;

import org.highfives.grid.review.command.aggregate.ReviewList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewListRepository extends JpaRepository<ReviewList, Integer> {


}
