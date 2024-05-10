package org.highfives.grid.review.command.service;

import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.repository.ReviewRepository;
import org.highfives.grid.review.command.aggregate.Review;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandReviewServiceImpl implements CommandReviewService{

    private final ReviewRepository reviewRepository;

    private final ModelMapper mapper;

    @Autowired
    public CommandReviewServiceImpl(ReviewRepository reviewRepository, ModelMapper mapper) {
        this.reviewRepository = reviewRepository;
        this.mapper = mapper;
    }

    @Override
    public ReviewDTO findReviewById(int id) {

        try {

            Review review = reviewRepository.findById(id).orElseThrow(() -> new IllegalAccessException());

            return mapper.map(review, ReviewDTO.class);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);

        }



    }
}
