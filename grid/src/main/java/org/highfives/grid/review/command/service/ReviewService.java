package org.highfives.grid.review.command.service;

import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO findReviewById(int id);

    ReviewDTO addReview(ReviewDTO reviewDTO);

    List<ReviewListDTO> findAllReview();

    ReviewHistoryDTO findReviewHistoryById(int id);

    ReviewHistoryDTO addReviewHistory(ReviewHistoryDTO historyDTO);

    ReviewHistoryDTO modifyReviewHistory(ReviewHistoryDTO historyDTO);

    ReviewListDTO addReviewList(ReviewListDTO reviewListDTO);

    ReviewListDTO modifyReviewList(ReviewListDTO reviewListDTO);

    void deleteReviewList(int id);

    void deleteReviewHistory(int id);

    ReviewDTO modifyReview(ReviewDTO reviewDTO);
}
