package org.highfives.grid.review.query.service;

import org.highfives.grid.review.query.aggregate.ReviewHistoryAndScore;
import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;
import org.highfives.grid.review.query.repository.ReviewMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "QueryReviewService")
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    private final ModelMapper mapper;

    @Autowired
    public ReviewServiceImpl(ReviewMapper reviewMapper, ModelMapper mapper) {
        this.reviewMapper = reviewMapper;
        this.mapper = mapper;
    }




    @Override
    public ReviewHistoryAndScoreDTO findHistoryAndScoreById(int historyId) {

        ReviewHistoryAndScore reviewHistoryAndScore =  reviewMapper.findHistoryAndScoreById(historyId);

        return mapper.map(reviewHistoryAndScore, ReviewHistoryAndScoreDTO.class);
    }
}
