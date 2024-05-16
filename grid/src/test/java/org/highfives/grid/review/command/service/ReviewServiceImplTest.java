package org.highfives.grid.review.command.service;

import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;
import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ReviewServiceImplTest {


    private final ReviewService commandReviewService;

    private final org.highfives.grid.review.query.service.ReviewService queryReviewService;
    @Autowired
    public ReviewServiceImplTest(ReviewService commandReviewService,
                                 org.highfives.grid.review.query.service.ReviewService queryReviewService) {
        this.commandReviewService = commandReviewService;
        this.queryReviewService = queryReviewService;
    }



    @Test
    @DisplayName("작성한 평가 조회")
    void findReviewById() {

        // Given

        int id = 1;

        // When
        ReviewDTO reviewById = commandReviewService.findReviewById(id);


        // Then

        assertThat(reviewById.getId()).isEqualTo(id);


    }

    @Test
    @DisplayName("평가 항목 리스트 전체 조회")
    void findAllReview() {

        // Given

        // When

        List<ReviewListDTO> reviewList = commandReviewService.findAllReview();


        // Then
        assertThat(reviewList).isNotNull();

    }

    @Test
    @DisplayName("평가 항목 내역 단일 조회")
    void findReviewHistoryById() {

        // Given

        int id = 1;

        // When

        ReviewHistoryDTO reviewHistoryDTO = commandReviewService.findReviewHistoryById(id);


        // Then
        assertThat(reviewHistoryDTO.getId()).isSameAs(id);

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

        ReviewHistoryDTO reviewHistoryDTO = commandReviewService.addReviewHistory(historyDTO);


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
                .score(85)
                .reviewId(1)
                .historyId(1)
                .build();

        // When

        ReviewDTO review = commandReviewService.addReview(reviewDTO);


        // Then

        assertThat(review.getScore()).isEqualTo(reviewDTO.getScore());

    }

    @Test
    @DisplayName("평가 내역 수정 기능")
    @Transactional
    void modifyReviewHistory() {

        // Given

        ReviewHistoryDTO reviewHistoryDTO = ReviewHistoryDTO.builder()
                .id(1)
                .content("update")
                .quarter(1)
                .year(2024)
                .reviewerId(2)
                .revieweeId(3)
                .build();

        // When
        ReviewHistoryDTO updateData = commandReviewService.modifyReviewHistory(reviewHistoryDTO);


        // Then
        assertThat(updateData.getReviewStatus()).isSameAs("COMPLETE");

    }

    @Test
    @DisplayName("평가 수정 기능")
    @Transactional
    void modifyReview() {
        // Given
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .id(1)
                .score(350)
                .build();

        // When
        ReviewDTO updateData = commandReviewService.modifyReview(reviewDTO);

        // Then
        assertThat(updateData.getScore()).isEqualTo(350);

    }

    @Test
    @DisplayName("평가 항목 추가 기능")
    @Transactional
    void addReviewList() {
        // Given
        ReviewListDTO reviewListDTO = ReviewListDTO.builder()
                .listName("test")
                .build();

        // When

        ReviewListDTO result = commandReviewService.addReviewList(reviewListDTO);

        // Then
        assertThat(result.getListName()).isEqualTo(result.getListName());
    }

    @Test
    @DisplayName("평가 항목 수정 기능")
    @Transactional
    void modifyReviewList() {
        // Given
        ReviewListDTO reviewListDTO = ReviewListDTO.builder()
                .id(1)
                .listName("2024년 상반기 테스트")
                .build();


        // When
        ReviewListDTO updateData = commandReviewService.modifyReviewList(reviewListDTO);

        // Then
        assertThat(updateData.getListName()).isEqualTo("2024년 상반기 테스트");


    }


    @Test
    @DisplayName("평가 항목 삭제 기능")
    @Transactional
    void deleteReviewList() {
        // Given
        int id = 1;


        // When
        commandReviewService.deleteReviewList(id);

        // Then
//        ReviewDTO result = reviewService.findReviewById(id);
//        assertThat(result).isNull();

    }

    @Test
    @DisplayName("동료 평가 결과 조회 기능")
    @Transactional
    void findHistoryAndScoreById(){
        // Given
        int historyId = 1;
        int reviewId = 1;

        // When
        List<ReviewHistoryAndScoreDTO> historyAndScoreById = queryReviewService.findHistoryAndScoreById(historyId, reviewId);
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
        List<org.highfives.grid.review.query.dto.ReviewHistoryDTO> assignedReviewByReviewerId =
                queryReviewService.findAssignedReviewByReviewerId(reviewerId);


        // Then
        for (org.highfives.grid.review.query.dto.ReviewHistoryDTO reviewHistoryDTO : assignedReviewByReviewerId) {
            assertThat(reviewHistoryDTO.getReviewerId()).isSameAs(reviewerId);
        }

    }
}