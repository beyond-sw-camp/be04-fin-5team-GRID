//package org.highfives.grid.performance_review.query.service;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class TotalPerformanceReviewServiceImplTest {
//
//    @Autowired
//    private TotalPerformanceReviewServiceImpl totalPerformanceReviewService;
//
//    @DisplayName("종합 평가 평가대상자 기준으로 조회")
//    @Test
//    public void findTotalByRevieweeId(){
//        int employeeId = 5;
//
//        assertNotNull(
//                totalPerformanceReviewService.findTotalByRevieweeId(employeeId)
//        );
//
//    }
//
//    @DisplayName("종합 평가 평가자 기준으로 조회")
//    @Test
//    public void findTotalByReviewerId(){
//        int employeeId = 3;
//
//        assertNotNull(
//                totalPerformanceReviewService.findTotalByReviewerId(employeeId)
//        );
//
//    }
//
//    @DisplayName("종합 평가 전체 조회")
//    @Test
//    public void findAllTotal(){
//
//        assertNotNull(
//                totalPerformanceReviewService.findAllTotal()
//        );
//
//    }
//
//    @DisplayName("종합 평가 하나 조회")
//    @Test
//    public void findTotalById(){
//        int id = 1;
//
//        assertNotNull(
//                totalPerformanceReviewService.findTotalById(id)
//        );
//    }
//}