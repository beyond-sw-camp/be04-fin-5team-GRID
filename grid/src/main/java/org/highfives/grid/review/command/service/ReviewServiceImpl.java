package org.highfives.grid.review.command.service;

import org.highfives.grid.review.command.aggregate.ReviewHistory;
import org.highfives.grid.review.command.aggregate.ReviewList;
import org.highfives.grid.review.command.aggregate.ReviewStatus;
import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;
import org.highfives.grid.review.command.exception.NotFoundException;
import org.highfives.grid.review.command.repository.ReviewHistoryRepository;
import org.highfives.grid.review.command.repository.ReviewListRepository;
import org.highfives.grid.review.command.repository.ReviewRepository;
import org.highfives.grid.review.command.aggregate.Review;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "CommandReviewService")
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewListRepository reviewListRepository;

    private final ReviewHistoryRepository reviewHistoryRepository;

    private final ModelMapper mapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewListRepository reviewListRepository,
                             ReviewHistoryRepository reviewHistoryRepository, ModelMapper mapper) {
        this.reviewRepository = reviewRepository;
        this.reviewListRepository = reviewListRepository;
        this.reviewHistoryRepository = reviewHistoryRepository;
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
    public ReviewDTO addReview(ReviewDTO reviewDTO) {

        Review review = Review.builder()
                .score(reviewDTO.getScore())
                .reviewId(reviewDTO.getReviewId())
                .historyId(reviewDTO.getHistoryId())
                .build();

        reviewRepository.save(review);
        return mapper.map(review, ReviewDTO.class);

    }

    @Override
    public List<ReviewListDTO> findAllReview() {

        List<ReviewList> reviewLists = reviewListRepository.findAll();
        if (reviewLists == null) {
            try {
                throw new NotFoundException("No reviews found");
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        List<ReviewListDTO> reviewListDTOs = new ArrayList<>();
        for (ReviewList reviewList : reviewLists) {
            reviewListDTOs.add(mapper.map(reviewList, ReviewListDTO.class));
        }
        return reviewListDTOs;
    }

    @Override
    public ReviewHistoryDTO findReviewHistoryById(int id) {


        ReviewHistory reviewHistory =  reviewHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());

        return mapper.map(reviewHistory, ReviewHistoryDTO.class);
    }

    @Override
    public ReviewHistoryDTO addReviewHistory(ReviewHistoryDTO historyDTO) {



        ReviewHistory reviewHistory = ReviewHistory.builder()
                .content(historyDTO.getContent())
                .year(historyDTO.getYear())
                .quarter(historyDTO.getQuarter())
                .reviewStatus(ReviewStatus.INCOMPLETE)
                .reviewerId(historyDTO.getReviewerId())
                .revieweeId(historyDTO.getRevieweeId())
                .build();


        reviewHistoryRepository.save(reviewHistory);



        return mapper.map(reviewHistory, ReviewHistoryDTO.class);
    }

    @Override
    public ReviewHistoryDTO modifyReviewHistory(ReviewHistoryDTO historyDTO) {
        ReviewHistory reviewHistory = reviewHistoryRepository.findById(historyDTO.getId()).orElseThrow(() -> new IllegalArgumentException());

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);

        ReviewHistory updateReviewHistory = ReviewHistory.builder()
                .id(historyDTO.getId())
                .content(reviewHistory.getContent())
                .year(reviewHistory.getYear())
                .quarter(reviewHistory.getQuarter())
                .reviewStatus(ReviewStatus.COMPLETE)
                .writeTime(formattedDate)
                .reviewerId(historyDTO.getReviewerId())
                .revieweeId(historyDTO.getRevieweeId())
                .build();

        reviewHistoryRepository.save(updateReviewHistory);

        return mapper.map(updateReviewHistory, ReviewHistoryDTO.class);
    }
}
