package org.highfives.grid.review.command.service;

import lombok.extern.slf4j.Slf4j;
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
import org.highfives.grid.user.command.aggregate.Employee;
import org.highfives.grid.user.command.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = "CommandReviewService")
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewListRepository reviewListRepository;

    private final ReviewHistoryRepository reviewHistoryRepository;

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewListRepository reviewListRepository,
                             ReviewHistoryRepository reviewHistoryRepository, UserRepository userRepository, ModelMapper mapper) {
        this.reviewRepository = reviewRepository;
        this.reviewListRepository = reviewListRepository;
        this.reviewHistoryRepository = reviewHistoryRepository;
        this.userRepository = userRepository;
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
    @Transactional
    public List<ReviewDTO> addReview(List<ReviewDTO> reviewDTO) {

        List<ReviewDTO> reviewList = new ArrayList<>();

        for (ReviewDTO reviewdata : reviewDTO) {
            Review review = Review.builder()
                    .score(reviewdata.getScore())
                    .reviewId(reviewdata.getReviewId())
                    .historyId(reviewdata.getHistoryId())
                    .build();
            reviewRepository.save(review);

            reviewList.add(mapper.map(review, ReviewDTO.class));
        }


        return reviewList;

    }

    @Override
    @Transactional
    public ReviewDTO modifyReview(ReviewDTO reviewDTO) {

        ReviewDTO currentData = findReviewById(reviewDTO.getId());

        Review review = Review.builder()
                .id(reviewDTO.getId())
                .score(reviewDTO.getScore())
                .reviewId(currentData.getReviewId())
                .historyId(currentData.getHistoryId())
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
    public ReviewListDTO addReviewList(ReviewListDTO reviewListDTO) {

        ReviewList reviewList = ReviewList.builder()
                .listName(reviewListDTO.getListName())
                .build();

        reviewListRepository.save(reviewList);

        return mapper.map(reviewList, ReviewListDTO.class);
    }

    @Override
    @Transactional
    public ReviewListDTO modifyReviewList(ReviewListDTO reviewListDTO) {

        ReviewList reviewList = ReviewList.builder()
                .id(reviewListDTO.getId())
                .listName(reviewListDTO.getListName())
                .build();

        reviewListRepository.save(reviewList);

        return mapper.map(reviewList, ReviewListDTO.class);
    }

    @Override
    public ReviewHistoryDTO findReviewHistoryById(int id) {


        ReviewHistory reviewHistory =  reviewHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());

        return mapper.map(reviewHistory, ReviewHistoryDTO.class);
    }

    @Override
    @Transactional
    public void deleteReviewList(int id) {

        reviewListRepository.deleteById(id);

    }

    @Override
    @Transactional
    public List<ReviewHistoryDTO> addReviewHistory(ReviewHistoryDTO historyDTO) {

        List<Employee> employeeList = userRepository.findAll();
        Map<Integer, List<Employee>> teamEmployeeMap = employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getTeamId));

        List<ReviewHistoryDTO> reviewHistoryDTOList = new ArrayList<>();

        for (Map.Entry<Integer, List<Employee>> entry : teamEmployeeMap.entrySet()) {
            List<Employee> teamEmployees = entry.getValue();

            // 팀 내 동료 평가 생성
            for (Employee reviewer : teamEmployees) {
                for (Employee reviewee : teamEmployees) {
                    if (reviewer.getId() == reviewee.getId()) {
                        continue;
                    }
                    ReviewHistory reviewHistory = ReviewHistory.builder()
                            .content(historyDTO.getContent())
                            .year(historyDTO.getYear())
                            .quarter(historyDTO.getQuarter())
                            .reviewStatus(ReviewStatus.N)
                            .writeTime(null)
                            .reviewerId(reviewer.getId())
                            .revieweeId(reviewee.getId())
                            .build();

                    reviewHistoryRepository.save(reviewHistory);
                    reviewHistoryDTOList.add(mapper.map(reviewHistory, ReviewHistoryDTO.class));
                }
            }
        }
        return reviewHistoryDTOList;
    }

    @Override
    @Transactional
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
                .reviewStatus(ReviewStatus.Y)
                .writeTime(formattedDate)
                .reviewerId(reviewHistory.getReviewerId())
                .revieweeId(reviewHistory.getRevieweeId())
                .build();

        reviewHistoryRepository.save(updateReviewHistory);

        return mapper.map(updateReviewHistory, ReviewHistoryDTO.class);

    }

    @Override
    @Transactional
    public void deleteReviewHistory(int id) {

        reviewHistoryRepository.deleteById(id);

    }


}
