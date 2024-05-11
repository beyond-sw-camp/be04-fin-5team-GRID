package org.highfives.grid.review.command.service;

import org.highfives.grid.review.command.aggregate.ReviewList;
import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;
import org.highfives.grid.review.command.repository.ReviewListRepository;
import org.highfives.grid.review.command.repository.ReviewRepository;
import org.highfives.grid.review.command.aggregate.Review;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandReviewServiceImpl implements CommandReviewService{

    private final ReviewRepository reviewRepository;

    private final ReviewListRepository reviewListRepository;

    private final ModelMapper mapper;

    @Autowired
    public CommandReviewServiceImpl(ReviewRepository reviewRepository, ReviewListRepository reviewListRepository, ModelMapper mapper) {
        this.reviewRepository = reviewRepository;
        this.reviewListRepository = reviewListRepository;
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

    @Override
    public ReviewDTO insertReviewById(ReviewDTO reviewDTO) {

        return null;

    }

    @Override
    public ReviewListDTO findAllReview() {

        List<ReviewList> reviewLists = reviewListRepository.findAll();

        return mapper.map(reviewLists, ReviewListDTO.class);
    }
}
