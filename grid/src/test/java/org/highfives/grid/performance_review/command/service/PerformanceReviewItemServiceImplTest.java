package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewItemDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PerformanceReviewItemServiceImplTest {

    @Autowired
    private PerformanceReviewItemServiceImpl performanceReviewItemService;

    // 항목 없는 업적 평가 있어야 테스트 가능
//    @DisplayName("평가 항목 생성")
//    @Test
//    @Transactional
//    public void addPerformanceReviewItems(){
//        int goalId = 3;
//        int reviewId = 5;
//        List<PerformanceReviewItemDTO> performanceReviewItemDTOList = performanceReviewItemService.addNewItems(goalId, reviewId);
//        System.out.println(performanceReviewItemDTOList);
//        assertNotNull(performanceReviewItemDTOList);
//    }

    @DisplayName("평가 항목 하나 수정")
    @Test
    @Transactional
    public void modifyPerformanceReviewItem(){
        PerformanceReviewItemDTO performanceReviewItemDTO = new PerformanceReviewItemDTO(
                1,
                "신규 모듈 추가를 위한 웹 애플리케이션 개발",
                "수정 3개월 안에 개발 완료",
                "신규 모듈 개발 완료한 수",
                "수정 3개월 안에 개발 완료",
                30,
                "수정 2023년 1분기 신규 모듈 개발 완료",
                1,
                28.5,
                "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
                1,
                28.5,
                1
        );

        System.out.println(performanceReviewItemDTO);

        PerformanceReviewItemDTO modifyItem = performanceReviewItemService.modifyItem(performanceReviewItemDTO);

        assertNotNull(modifyItem);
    }

    @DisplayName("reviewId로 평가 항목 조회")
    @Test
    public void findPerformanceReviewItemByReviewId(){
        int id = 1;

        List<PerformanceReviewItemDTO> performanceReviewItemDTOList = performanceReviewItemService.findByReviewId(id);

        assertNotNull(performanceReviewItemDTOList);
    }
}