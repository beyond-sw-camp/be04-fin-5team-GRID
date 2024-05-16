package org.highfives.grid.review.query.service;

import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;
import org.highfives.grid.review.query.dto.ReviewHistoryDTO;

import java.util.List;

public interface ReviewService {


    List<ReviewHistoryAndScoreDTO> findHistoryAndScoreById(int historyId, int revieweeId);

    List<ReviewHistoryDTO> findAssignedReviewByReviewerId(int reviewerId);


}
