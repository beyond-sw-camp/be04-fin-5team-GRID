package org.highfives.grid.review.query.service;

import com.github.pagehelper.PageInfo;
import jakarta.transaction.Transactional;
import org.highfives.grid.review.query.dto.ReviewEmployeesHistoryDTO;
import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;
import org.highfives.grid.review.query.dto.ReviewHistoryDTO;
import org.highfives.grid.review.query.dto.ReviewListDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceImplTest {
    private final ReviewService reviewService;

    @Autowired
    public ReviewServiceImplTest(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @Test
    @DisplayName("동료 평가 결과 조회 기능")
    @Transactional
    void findHistoryAndScoreById(){
        // Given
        int historyId = 1;
        int reviewId = 1;

        // When
        List<ReviewHistoryAndScoreDTO> historyAndScoreById = reviewService.findHistoryAndScoreById(historyId, reviewId);

        // Then
        assertThat(historyAndScoreById).isNotNull();

        for (ReviewHistoryAndScoreDTO reviewHistoryAndScoreDTO : historyAndScoreById) {
            List<org.highfives.grid.review.query.dto.ReviewDTO> reviewInfoList = reviewHistoryAndScoreDTO.getReviewInfo();

            for (org.highfives.grid.review.query.dto.ReviewDTO reviewDTO : reviewInfoList) {
                assertThat(reviewDTO.getHistoryId()).isEqualTo(historyId);
                assertThat(reviewDTO.getReviewId()).isEqualTo(reviewId);
            }
        }
    }


    @Test
    @DisplayName("배정된 동료 평가 조회 기능")
    void findAssignedReviewByReviewerId(){
        // Given
        int reviewerId = 1;

        // When
        List<ReviewHistoryDTO> assignedReviewByReviewerId =
                reviewService.findAssignedReviewByReviewerId(reviewerId);


        // Then
        for (ReviewHistoryDTO reviewHistoryDTO : assignedReviewByReviewerId) {
            assertThat(reviewHistoryDTO.getReviewerId()).isSameAs(reviewerId);
        }

    }

    @Test
    void findHistoryList() {
        // Given
        int reviewerId = 1;

        // When
        List<ReviewHistoryDTO> historyList = reviewService.findHistoryList();


        // Then
    }

    @Test
    void findMyReviewHistory() {
        // Given
        int reviewerId = 1;

        // When
        List<ReviewHistoryDTO> myReviewHistory = reviewService.findMyReviewHistory(reviewerId);


        // Then
    }

    @Test
    void findReviewList() {
        // Given
        int reviewerId = 1;

        // When
        ReviewListDTO reviewList = reviewService.findReviewList(reviewerId);


        // Then
    }

    @Test
    void findEmployeesHistory() {
        // Given
        int page = 10;
        int size = 10;
        // When
        PageInfo<ReviewEmployeesHistoryDTO> employeesHistory = reviewService.findEmployeesHistory(page,size);


        // Then
    }
}