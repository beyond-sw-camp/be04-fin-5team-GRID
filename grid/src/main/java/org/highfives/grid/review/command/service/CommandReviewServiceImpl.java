package org.highfives.grid.review.command.service;

import org.highfives.grid.review.command.aggregate.ReviewHistory;
import org.highfives.grid.review.command.aggregate.ReviewList;
import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;
import org.highfives.grid.review.command.repository.ReviewHistoryRepository;
import org.highfives.grid.review.command.repository.ReviewListRepository;
import org.highfives.grid.review.command.repository.ReviewRepository;
import org.highfives.grid.review.command.aggregate.Review;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CommandReviewServiceImpl implements CommandReviewService{

    private final ReviewRepository reviewRepository;

    private final ReviewListRepository reviewListRepository;

    private final ReviewHistoryRepository reviewHistoryRepository;

    private final ModelMapper mapper;

    @Autowired
    public CommandReviewServiceImpl(ReviewRepository reviewRepository, ReviewListRepository reviewListRepository,
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
    public ReviewDTO insertReviewById(ReviewDTO reviewDTO) {

        return null;

    }

    @Override
    public ReviewListDTO findAllReview() {

        List<ReviewList> reviewLists = reviewListRepository.findAll();

        return mapper.map(reviewLists, ReviewListDTO.class);
    }

    @Override
    public ReviewHistoryDTO findReviewHistoryById(int id) {


        ReviewHistory reviewHistory =  reviewHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());

        return mapper.map(reviewHistory, ReviewHistoryDTO.class);
    }

    @Override
    public ReviewHistoryDTO addReviewHistory(ReviewHistoryDTO historyDTO) {

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(currentDate);

        ReviewHistory reviewHistory = ReviewHistory.builder()
                .content(historyDTO.getContent())
                .year(historyDTO.getYear())
                .quarter(historyDTO.getQuarter())
                .reviewStatus("Y")
                .writeTime(formattedDate)
                .reviewerId(historyDTO.getReviewerId())
                .revieweeId(historyDTO.getRevieweeId())
                .build();


        reviewHistoryRepository.save(reviewHistory);



        return mapper.map(reviewHistory, ReviewHistoryDTO.class);
    }
}
