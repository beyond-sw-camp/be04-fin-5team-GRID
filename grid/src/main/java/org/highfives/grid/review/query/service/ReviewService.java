package org.highfives.grid.review.query.service;

import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;

public interface ReviewService {


    ReviewHistoryAndScoreDTO findHistoryAndScoreById(int historyId);
}
