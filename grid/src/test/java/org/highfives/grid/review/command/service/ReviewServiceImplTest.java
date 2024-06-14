//package org.highfives.grid.review.command.service;
//
//import jakarta.transaction.Transactional;
//import org.highfives.grid.review.command.dto.ReviewDTO;
//import org.highfives.grid.review.command.dto.ReviewHistoryDTO;
//import org.highfives.grid.review.command.dto.ReviewListDTO;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//class ReviewServiceImplTest {
//
//
//    private final ReviewService reviewService;
//
//    @Autowired
//    public ReviewServiceImplTest(ReviewService reviewService) {
//        this.reviewService = reviewService;
//    }
//
//
//
//
//
//
//
//    @Test
//    @DisplayName("작성한 평가 조회")
//    void findReviewById() {
//
//        // Given
//
//        int id = 1;
//
//        // When
//        ReviewDTO reviewById = reviewService.findReviewById(id);
//
//
//        // Then
//
//        assertThat(reviewById.getId()).isEqualTo(id);
//
//
//    }
//
//    @Test
//    @DisplayName("평가 후 결과 저장 기능")
//    @Transactional
//    void addReview() {
//
//        // Given
//        List<ReviewDTO> ReviewDTOList = new ArrayList<>();
//
//        ReviewDTO reviewDTO = ReviewDTO.builder()
//                .score(85)
//                .reviewId(1)
//                .historyId(1)
//                .build();
//
//        ReviewDTOList.add(reviewDTO);
//
//        // When
//
//        List<ReviewDTO> testDataList = reviewService.addReview(ReviewDTOList);
//
//
//        // Then
//        for (ReviewDTO testData : testDataList) {
//            assertThat(testData.getScore()).isNotNull();
//        }
//
//
//
//    }
//
//    @Test
//    @DisplayName("평가 수정 기능")
//    @Transactional
//    void modifyReview() {
//        // Given
//        ReviewDTO reviewDTO = ReviewDTO.builder()
//                .id(1)
//                .score(100)
//                .build();
//
//        // When
//        ReviewDTO updateData = reviewService.modifyReview(reviewDTO);
//
//        // Then
//        assertThat(updateData.getScore()).isEqualTo(100);
//
//    }
//
//    @Test
//    @DisplayName("평가 항목 리스트 전체 조회")
//    void findAllReview() {
//
//        // Given
//
//        // When
//
//        List<ReviewListDTO> reviewList = reviewService.findAllReview();
//
//
//        // Then
//        assertThat(reviewList).isNotNull();
//
//    }
//
//    @Test
//    @DisplayName("평가 항목 추가 기능")
//    @Transactional
//    void addReviewList() {
//        // Given
//        ReviewListDTO reviewListDTO = ReviewListDTO.builder()
//                .listName("test")
//                .build();
//
//        // When
//
//        ReviewListDTO result = reviewService.addReviewList(reviewListDTO);
//
//        // Then
//        assertThat(result.getListName()).isEqualTo(result.getListName());
//    }
//
//    @Test
//    @DisplayName("평가 항목 수정 기능")
//    @Transactional
//    void modifyReviewList() {
//        // Given
//        ReviewListDTO reviewListDTO = ReviewListDTO.builder()
//                .id(1)
//                .listName("2024년 상반기 테스트")
//                .build();
//
//
//        // When
//        ReviewListDTO updateData = reviewService.modifyReviewList(reviewListDTO);
//
//        // Then
//        assertThat(updateData.getListName()).isEqualTo("2024년 상반기 테스트");
//
//
//    }
//    @Test
//    @DisplayName("평가 항목 내역 단일 조회")
//    void findReviewHistoryById() {
//
//        // Given
//
//        int id = 1;
//
//        // When
//
//        ReviewHistoryDTO reviewHistoryDTO = reviewService.findReviewHistoryById(id);
//
//
//        // Then
//        assertThat(reviewHistoryDTO.getId()).isSameAs(id);
//
//    }
//
//    @Test
//    @DisplayName("평가 항목 삭제 기능")
//    @Transactional
//    void deleteReviewList() {
//
//
//        //Given
//
//        int id = 1;
//
//        // When
//
//        reviewService.deleteReviewList(id);
//    }
//
//    @Test
//    @DisplayName("평가 후 평가 항목 내역 추가 기능")
//    @Transactional
//    void addReviewHistory() {
//
//        // Given
//
//        ReviewHistoryDTO historyDTO = ReviewHistoryDTO.builder()
//                .content("test")
//                .quarter(1)
//                .year(2024)
//                .build();
//
//        // When
//
//        List<ReviewHistoryDTO> testDataList = reviewService.addReviewHistory(historyDTO);
//
//
//        // Then
//        for (ReviewHistoryDTO testData : testDataList) {
//            assertThat(testData).isNotNull();
//        }
//
//
//    }
//
//    @Test
//    @DisplayName("평가 내역 수정 기능")
//    @Transactional
//    void modifyReviewHistory() {
//
//        // Given
//
//        ReviewHistoryDTO reviewHistoryDTO = ReviewHistoryDTO.builder()
//                .id(1)
//                .content("update")
//                .quarter(1)
//                .year(2024)
//                .reviewerId(2)
//                .revieweeId(3)
//                .build();
//
//        // When
//        ReviewHistoryDTO updateData = reviewService.modifyReviewHistory(reviewHistoryDTO);
//
//
//        // Then
//        assertThat(updateData.getReviewStatus()).isSameAs("Y");
//
//    }
//
//    @Test
//    @DisplayName("평가 내역 삭제 기능")
//    @Transactional
//    void deleteReviewHistory() {
//
//        //Given
//
//        int id = 1;
//
//        // When
//
//        reviewService.deleteReviewHistory(id);
//
//    }
//}