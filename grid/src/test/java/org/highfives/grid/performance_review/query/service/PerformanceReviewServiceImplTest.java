//package org.highfives.grid.performance_review.query.service;
//
//import org.highfives.grid.performance_review.query.dto.DetailPerformanceReviewDTO;
//import org.highfives.grid.performance_review.query.dto.PerformanceReviewDTO;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class PerformanceReviewServiceImplTest {
//
//    @Autowired
//    private PerformanceReviewServiceImpl performanceReviewService;
//
//    @DisplayName("5번 직원의 작성한 평가항목 조회")
//    @Test
//    public void findReviewByWriterId() {
//        int id = 5;
//        List<PerformanceReviewDTO> findReview = performanceReviewService.findPerformanceReviewByWriterId(id);
//
//        System.out.println(findReview);
//        assertNotNull(findReview);
//    }
//
//    @DisplayName("3번 직원의 결재가 필요한 평가항목 조회")
//    @Test
//    public void findReviewByApproverId() {
//        int id = 3;
//        List<PerformanceReviewDTO> findReview = performanceReviewService.findPerformanceReviewByApproverId(id);
//
//        assertNotNull(findReview);
//    }
//
//    @DisplayName("1번 평가 상세 조회")
//    @Test
//    public void findDetailReviewById(){
//        int id = 1;
//        DetailPerformanceReviewDTO findReview = performanceReviewService.findDetailPerformanceReviewById(id);
//
//        System.out.println(findReview);
//        assertNotNull(findReview);
//    }
//}