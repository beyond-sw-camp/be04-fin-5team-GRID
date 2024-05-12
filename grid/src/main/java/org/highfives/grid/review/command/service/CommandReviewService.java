package org.highfives.grid.review.command.service;

import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;

public interface CommandReviewService {
    ReviewDTO findReviewById(int id);

    ReviewDTO insertReviewById(ReviewDTO reviewDTO);

    ReviewListDTO findAllReview();

    ReviewHistoryDTO findReviewHistoryById(int id);

    ReviewHistoryDTO addReviewHistory(ReviewHistoryDTO historyDTO);
}
