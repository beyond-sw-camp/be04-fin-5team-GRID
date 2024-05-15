package org.highfives.grid.review.query.service;

import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;
import org.highfives.grid.review.query.repository.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "QueryReviewService")
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;



    @Autowired
    public ReviewServiceImpl(ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
    }


    @Override
    public ReviewHistoryAndScoreDTO findHistoryAndScoreById(int historyId) {

        ReviewHistoryAndScoreDTO reviewHistoryAndScoreDTO =  reviewMapper.findHistoryAndScoreById(historyId);

        return reviewHistoryAndScoreDTO;
    }
}
