//package org.highfives.grid.performance_review.command.service;
//
//import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
//import org.highfives.grid.performance_review.command.dto.TotalPerformanceReviewDTO;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class TotalPerformanceReviewServiceImplTest {
//
//    @Autowired
//    private TotalPerformanceReviewServiceImpl totalPerformanceReviewService;
//
//    @DisplayName("종합 평가 추가")
//    @Test
//    @Transactional
//    void addTotalReview() {
//        List<PerformanceReviewDTO> performanceReviewDTOList = new ArrayList<>();
//
//        performanceReviewDTOList.add(new PerformanceReviewDTO(
//                3,
//                "M",
//                2024,
//                "2024년 인사평가",
//                "V",
//                12,
//                "2024-06-01 12:00:00",
//                10,
//                "2024-06-03 12:00:00",
//                2
//        ));
//
//        performanceReviewDTOList.add(new PerformanceReviewDTO(
//                4,
//                "F",
//                2024,
//                "2023년 인사평가",
//                "V",
//                12,
//                "2024-12-01 12:00:00",
//                10,
//                "2024-12-02 12:00:00",
//                2
//        ));
//
//        TotalPerformanceReviewDTO total = totalPerformanceReviewService.addTotalReview(performanceReviewDTOList);
//
//        System.out.println(total);
//        assertNotNull(total);
//    }
//}