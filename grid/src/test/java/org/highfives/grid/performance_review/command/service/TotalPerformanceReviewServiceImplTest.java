package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.TotalPerformanceReviewDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TotalPerformanceReviewServiceImplTest {

    @Autowired
    private TotalPerformanceReviewServiceImpl totalPerformanceReviewService;

    @DisplayName("종합 평가 추가")
    @Test
    @Transactional
    void addTotalReview() {
        List<PerformanceReviewDTO> performanceReviewDTOList = new ArrayList<>();

        performanceReviewDTOList.add(new PerformanceReviewDTO(
                5,
                "M",
                2023,
                "2023년 인사평가",
                "V",
                6,
                "2024-06-01 12:00:00",
                5,
                "2024-06-03 12:00:00",
                4
        ));

        performanceReviewDTOList.add(new PerformanceReviewDTO(
                6,
                "F",
                2023,
                "2023년 인사평가",
                "V",
                6,
                "2024-12-01 12:00:00",
                5,
                "2024-12-02 12:00:00",
                4
        ));

        TotalPerformanceReviewDTO total = totalPerformanceReviewService.addTotalReview(performanceReviewDTOList);

        System.out.println(total);
        assertNotNull(total);
    }
}