package org.highfives.grid.review.command.service;

import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ReviewServiceImplTest {


    private final ReviewService reviewService;

    @Autowired
    public ReviewServiceImplTest(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Test
    @DisplayName("작성한 평가 조회")
    void findReviewById() {

        // Given

        int id = 1;

        // When
        ReviewDTO reviewById = reviewService.findReviewById(id);


        // Then

        assertThat(reviewById.getReviewId()).isEqualTo(id);


    }

    @Test
    @DisplayName("평가 항목 리스트 전체 조회")
    void findAllReview() {

        // Given

        // When

        ReviewListDTO reviewList = reviewService.findAllReview();


        // Then
        assertThat(reviewList).isNotNull();

    }

    @Test
    @DisplayName("평가 항목 내역 단일 조회")
    void findReviewHistoryById() {

        // Given

        int id = 1;

        // When

        ReviewHistoryDTO reviewHistoryDTO = reviewService.findReviewHistoryById(id);


        // Then
        assertThat(reviewHistoryDTO.getReviewerId()).isSameAs(id);

    }

    @Test
    @DisplayName("평가 후 평가 항목 내역 추가 기능")
    @Transactional
    void addReviewHistory() {

        // Given

        ReviewHistoryDTO historyDTO = ReviewHistoryDTO.builder()
                .content("test")
                .quarter(1)
                .year(2024)
                .revieweeId(1)
                .reviewerId(3)
                .build();

        // When

        ReviewHistoryDTO reviewHistoryDTO = reviewService.addReviewHistory(historyDTO);


        // Then
        assertThat(reviewHistoryDTO.getReviewerId()).isSameAs(historyDTO.getReviewerId());
        assertThat(reviewHistoryDTO.getRevieweeId()).isSameAs(historyDTO.getRevieweeId());

    }

    @Test
    @DisplayName("평가 후 결과 저장 기능")
    @Transactional
    void addReview() {

        // Given

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .score(400)
                .reviewId(1)
                .historyId(1)
                .build();

        // When

        ReviewDTO review = reviewService.addReview(reviewDTO);


        // Then

        assertThat(review.getScore()).isEqualTo(reviewDTO.getScore());

    }
}