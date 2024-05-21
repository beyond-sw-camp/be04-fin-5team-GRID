package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.dto.ModifyPerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewItemDTO;
import org.highfives.grid.performance_review.command.vo.RequestPerformanceReviewVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PerformanceReviewServiceImplTest {

    @Autowired
    private PerformanceReviewServiceImpl performanceReviewService;

    @DisplayName("업적 평가 생성")
    @Test
    @Transactional
    public void addNewPerformanceReview(){
        PerformanceReviewDTO performanceReviewDTO = new PerformanceReviewDTO(
                "M",
                2024,
                "2024 인사평가",
                8
        );

        PerformanceReviewDTO addReview = performanceReviewService.addNewPerformanceReview(performanceReviewDTO);

        System.out.println(addReview);
        assertNotNull(addReview);
    }

    @DisplayName("업적 평가 저장")
    @Test
    @Transactional
    public void modifyPerformanceReviewStatusInProgress(){

        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
        performanceReviewItemList.add(
                new PerformanceReviewItemDTO( 1,
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
                        1)
        );


        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                2,
                "웹 애플리케이션의 품질을 보장하기 위한 테스트 수행",
                "수정 매주 정기적인 테스트 수행",
                "발견된 버그 수정 및 재테스트 완료한 비율",
                "매주 정기적인 테스트 수행",
                20,
                "수정 2023년 1분기 테스트 완료율 유지",
                1,
                19.0,
                "수정 매주 정기적으로 테스트를 수행하여 품질을 유지하고, 발견된 문제를 신속하게 해결하였습니다.",
                2,
                17.0,
                1
        ));

        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                3,
                "신규 기능을 안정적으로 배포하기 위한 작업",
                "수정 매주 정기적인 배포",
                "배포된 버전의 안정성 지표",
                "수정 매주 정기적인 배포",
                50,
                "2023년 1분기 안정적인 배포 진행",
                1,
                47.5,
                "수정 신규 기능을 안정적으로 배포하여 사용자들에게 원활한 서비스를 제공하였습니다.",
                1,
                47.5,
                1
        ));

        int reviewId = 1;

        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
                reviewId,
                performanceReviewItemList
        );

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusInProgress(requestPerformanceReviewVO);
        System.out.println(modifyPerformanceReviewDTO);

        assertNotNull(modifyPerformanceReviewDTO);
    }

    @DisplayName("업적 평가 저장")
    @Test
    @Transactional
    public void modifyPerformanceReviewStatusSubmit(){

        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
        performanceReviewItemList.add(
                new PerformanceReviewItemDTO( 1,
                        "신규 모듈 추가를 위한 웹 애플리케이션 개발",
                        "상신 3개월 안에 개발 완료",
                        "신규 모듈 개발 완료한 수",
                        "수정 3개월 안에 개발 완료",
                        30,
                        "수정 2023년 1분기 신규 모듈 개발 완료",
                        1,
                        28.5,
                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
                        1,
                        28.5,
                        1)
        );


        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                2,
                "웹 애플리케이션의 품질을 보장하기 위한 테스트 수행",
                "상신 매주 정기적인 테스트 수행",
                "발견된 버그 수정 및 재테스트 완료한 비율",
                "매주 정기적인 테스트 수행",
                20,
                "수정 2023년 1분기 테스트 완료율 유지",
                1,
                19.0,
                "수정 매주 정기적으로 테스트를 수행하여 품질을 유지하고, 발견된 문제를 신속하게 해결하였습니다.",
                2,
                17.0,
                1
        ));

        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                3,
                "신규 기능을 안정적으로 배포하기 위한 작업",
                "상신 매주 정기적인 배포",
                "배포된 버전의 안정성 지표",
                "수정 매주 정기적인 배포",
                50,
                "2023년 1분기 안정적인 배포 진행",
                1,
                47.5,
                "수정 신규 기능을 안정적으로 배포하여 사용자들에게 원활한 서비스를 제공하였습니다.",
                1,
                47.5,
                1
        ));

        int reviewId = 1;

        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
                reviewId,
                performanceReviewItemList
        );

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusSubmit(requestPerformanceReviewVO);
        System.out.println(modifyPerformanceReviewDTO);

        assertNotNull(modifyPerformanceReviewDTO);
    }

    @DisplayName("업적 평가 확인 중")
    @Test
    @Transactional
    public void modifyPerformanceReviewStatusRead(){

        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
        performanceReviewItemList.add(
                new PerformanceReviewItemDTO( 1,
                        "신규 모듈 추가를 위한 웹 애플리케이션 개발",
                        "3개월 안에 개발 완료",
                        "신규 모듈 개발 완료한 수",
                        "수정 3개월 안에 개발 완료",
                        30,
                        "수정 2023년 1분기 신규 모듈 개발 완료",
                        1,
                        28.5,
                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
                        1,
                        28.5,
                        1)
        );


        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                2,
                "웹 애플리케이션의 품질을 보장하기 위한 테스트 수행",
                "매주 정기적인 테스트 수행",
                "발견된 버그 수정 및 재테스트 완료한 비율",
                "매주 정기적인 테스트 수행",
                20,
                "수정 2023년 1분기 테스트 완료율 유지",
                1,
                19.0,
                "수정 매주 정기적으로 테스트를 수행하여 품질을 유지하고, 발견된 문제를 신속하게 해결하였습니다.",
                2,
                17.0,
                1
        ));

        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                3,
                "신규 기능을 안정적으로 배포하기 위한 작업",
                "매주 정기적인 배포",
                "배포된 버전의 안정성 지표",
                "수정 매주 정기적인 배포",
                50,
                "2023년 1분기 안정적인 배포 진행",
                1,
                47.5,
                "수정 신규 기능을 안정적으로 배포하여 사용자들에게 원활한 서비스를 제공하였습니다.",
                1,
                47.5,
                1
        ));

        int reviewId = 1;

        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
                reviewId,
                performanceReviewItemList
        );

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusRead(requestPerformanceReviewVO);
        System.out.println(modifyPerformanceReviewDTO);

        assertNotNull(modifyPerformanceReviewDTO);
    }

    @DisplayName("업적 평가 완료")
    @Test
    @Transactional
    public void modifyPerformanceReviewStatusComplete(){

        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
        performanceReviewItemList.add(
                new PerformanceReviewItemDTO( 1,
                        "신규 모듈 추가를 위한 웹 애플리케이션 개발",
                        "3개월 안에 개발 완료",
                        "신규 모듈 개발 완료한 수",
                        "수정 3개월 안에 개발 완료",
                        30,
                        "수정 2023년 1분기 신규 모듈 개발 완료",
                        1,
                        28.5,
                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
                        1,
                        28.5,
                        1)
        );


        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                2,
                "웹 애플리케이션의 품질을 보장하기 위한 테스트 수행",
                "매주 정기적인 테스트 수행",
                "발견된 버그 수정 및 재테스트 완료한 비율",
                "매주 정기적인 테스트 수행",
                20,
                "수정 2023년 1분기 테스트 완료율 유지",
                1,
                19.0,
                "수정 매주 정기적으로 테스트를 수행하여 품질을 유지하고, 발견된 문제를 신속하게 해결하였습니다.",
                2,
                17.0,
                1
        ));

        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                3,
                "신규 기능을 안정적으로 배포하기 위한 작업",
                "매주 정기적인 배포",
                "배포된 버전의 안정성 지표",
                "수정 매주 정기적인 배포",
                50,
                "2023년 1분기 안정적인 배포 진행",
                1,
                47.5,
                "수정 신규 기능을 안정적으로 배포하여 사용자들에게 원활한 서비스를 제공하였습니다.",
                1,
                47.5,
                1
        ));

        int reviewId = 1;

        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
                reviewId,
                performanceReviewItemList
        );

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusComplete(requestPerformanceReviewVO);
        System.out.println(modifyPerformanceReviewDTO);

        assertNotNull(modifyPerformanceReviewDTO);
    }

    @DisplayName("중간 업적 평가 확정")
    @Test
    @Transactional
    public void modifyPerformanceReviewStatusValid(){

        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
        performanceReviewItemList.add(
                new PerformanceReviewItemDTO( 1,
                        "신규 모듈 추가를 위한 웹 애플리케이션 개발",
                        "3개월 안에 개발 완료",
                        "신규 모듈 개발 완료한 수",
                        "수정 3개월 안에 개발 완료",
                        30,
                        "수정 2023년 1분기 신규 모듈 개발 완료",
                        1,
                        28.5,
                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
                        1,
                        28.5,
                        1)
        );


        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                2,
                "웹 애플리케이션의 품질을 보장하기 위한 테스트 수행",
                "매주 정기적인 테스트 수행",
                "발견된 버그 수정 및 재테스트 완료한 비율",
                "매주 정기적인 테스트 수행",
                20,
                "수정 2023년 1분기 테스트 완료율 유지",
                1,
                19.0,
                "수정 매주 정기적으로 테스트를 수행하여 품질을 유지하고, 발견된 문제를 신속하게 해결하였습니다.",
                2,
                17.0,
                1
        ));

        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                3,
                "신규 기능을 안정적으로 배포하기 위한 작업",
                "매주 정기적인 배포",
                "배포된 버전의 안정성 지표",
                "수정 매주 정기적인 배포",
                50,
                "2023년 1분기 안정적인 배포 진행",
                1,
                47.5,
                "수정 신규 기능을 안정적으로 배포하여 사용자들에게 원활한 서비스를 제공하였습니다.",
                1,
                47.5,
                1
        ));

        int reviewId = 1;

        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
                reviewId,
                performanceReviewItemList
        );

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusValid(requestPerformanceReviewVO);
        System.out.println(modifyPerformanceReviewDTO);

        assertNotNull(modifyPerformanceReviewDTO);
    }

    @DisplayName("연말 평가 확정")
    @Test
    @Transactional
    public void modifyPerformanceReviewStatusValid2(){

        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
        performanceReviewItemList.add(
                new PerformanceReviewItemDTO( 7,
                        "신규 모듈 추가를 위한 웹 애플리케이션 개발",
                        "3개월 안에 개발 완료",
                        "신규 모듈 개발 완료한 수",
                        "수정 3개월 안에 개발 완료",
                        30,
                        "수정 2023년 1분기 신규 모듈 개발 완료",
                        1,
                        28.5,
                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
                        1,
                        28.5,
                        3)
        );


        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                8,
                "웹 애플리케이션의 품질을 보장하기 위한 테스트 수행",
                "매주 정기적인 테스트 수행",
                "발견된 버그 수정 및 재테스트 완료한 비율",
                "매주 정기적인 테스트 수행",
                20,
                "수정 2023년 1분기 테스트 완료율 유지",
                1,
                19.0,
                "수정 매주 정기적으로 테스트를 수행하여 품질을 유지하고, 발견된 문제를 신속하게 해결하였습니다.",
                2,
                17.0,
                3
        ));

        performanceReviewItemList.add(new PerformanceReviewItemDTO(
                9,
                "신규 기능을 안정적으로 배포하기 위한 작업",
                "매주 정기적인 배포",
                "배포된 버전의 안정성 지표",
                "수정 매주 정기적인 배포",
                50,
                "2023년 1분기 안정적인 배포 진행",
                1,
                47.5,
                "수정 신규 기능을 안정적으로 배포하여 사용자들에게 원활한 서비스를 제공하였습니다.",
                1,
                47.5,
                3
        ));

        int reviewId = 3;

        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
                reviewId,
                performanceReviewItemList
        );

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusValid(requestPerformanceReviewVO);
        System.out.println(modifyPerformanceReviewDTO);

        assertNotNull(modifyPerformanceReviewDTO);
    }

}