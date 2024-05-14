package org.highfives.grid.review.command.repository;

import org.highfives.grid.review.command.aggregate.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {


}
