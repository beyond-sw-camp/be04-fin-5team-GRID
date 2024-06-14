//package org.highfives.grid.performance_review.command.service;
//
//import org.highfives.grid.performance_review.command.dto.*;
//import org.highfives.grid.performance_review.command.vo.RequestPerformanceReviewVO;
//import org.highfives.grid.performance_review.query.dto.DetailPerformanceReviewDTO;
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
//class PerformanceReviewServiceImplTest {
//
//    private PerformanceReviewServiceImpl performanceReviewService;
//    private PerformanceReviewGoalServiceImpl performanceReviewGoalService;
//    private PerformanceReviewGoalItemServiceImpl performanceReviewGoalItemService;
//
//    private org.highfives.grid.performance_review.query.service.PerformanceReviewServiceImpl queryPerformanceReviewService;
//
//    @Autowired
//    public PerformanceReviewServiceImplTest(PerformanceReviewServiceImpl performanceReviewService,
//                                            PerformanceReviewGoalServiceImpl performanceReviewGoalService,
//                                            PerformanceReviewGoalItemServiceImpl performanceReviewGoalItemService,
//                                            org.highfives.grid.performance_review.query.service.PerformanceReviewServiceImpl queryPerformanceReviewService) {
//        this.performanceReviewService = performanceReviewService;
//        this.performanceReviewGoalService = performanceReviewGoalService;
//        this.performanceReviewGoalItemService = performanceReviewGoalItemService;
//        this.queryPerformanceReviewService = queryPerformanceReviewService;
//    }
//
//    @DisplayName("업적 평가 생성")
//    @Test
//    @Transactional
//    public void addNewPerformanceReview(){
//        PerformanceReviewGoalDTO performanceReviewGoalDTO = new PerformanceReviewGoalDTO(
//                2025,
//                "2025 인사평가",
//                "A",
//                4,
//                "2025-03-01 08:00:00",
//                3
//        );
//
//        PerformanceReviewGoalDTO saveGoal = performanceReviewGoalService.addNewGoal(performanceReviewGoalDTO);
//        int id = saveGoal.getId();
//
//        PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO = new PerformanceReviewGoalItemDTO(
//                null,
//                "웹 애플리케이션 보안 강화",
//                "웹 애플리케이션의 보안 취약점을 보완하기 위한 작업",
//                "해결된 보안 취약점 수",
//                40,
//                "매월 보안 취약점 스캔",
//                null,
//                id
//        );
//
//        PerformanceReviewDTO performanceReviewDTO = new PerformanceReviewDTO(
//                "M",
//                2025,
//                "2025 인사평가",
//                4
//        );
//
//        PerformanceReviewDTO addReview = performanceReviewService.addNewPerformanceReview(performanceReviewDTO);
//
//        System.out.println(addReview);
//        assertNotNull(addReview);
//    }
//
//    @DisplayName("업적 평가 저장")
//    @Test
//    @Transactional
//    public void modifyPerformanceReviewStatusInProgress(){
//        PerformanceReviewGoalDTO performanceReviewGoalDTO = new PerformanceReviewGoalDTO(
//                2025,
//                "2025 인사평가",
//                "A",
//                4,
//                "2025-03-01 08:00:00",
//                3
//        );
//
//        PerformanceReviewGoalDTO saveGoal = performanceReviewGoalService.addNewGoal(performanceReviewGoalDTO);
//        int id = saveGoal.getId();
//
//        PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO = new PerformanceReviewGoalItemDTO(
//                null,
//                "웹 애플리케이션 보안 강화",
//                "웹 애플리케이션의 보안 취약점을 보완하기 위한 작업",
//                "해결된 보안 취약점 수",
//                100,
//                "매월 보안 취약점 스캔",
//                null,
//                id
//        );
//
//        performanceReviewGoalItemService.addGoalItem(performanceReviewGoalItemDTO);
//
//        PerformanceReviewDTO performanceReviewDTO = new PerformanceReviewDTO(
//                "M",
//                2025,
//                "2025 인사평가",
//                4
//        );
//
//        PerformanceReviewDTO addReview = performanceReviewService.addNewPerformanceReview(performanceReviewDTO);
//
//        System.out.println("평가조회"+addReview);
//
//        DetailPerformanceReviewDTO detailReview = queryPerformanceReviewService.findDetailPerformanceReviewById(addReview.getId());
//
//        System.out.println("상세 조회" + detailReview);
//        System.out.println();
//        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
//        performanceReviewItemList.add(
//                new PerformanceReviewItemDTO( detailReview.getReviewItemList().get(0).getId(),
//                        detailReview.getReviewItemList().get(0).getGoal(),
//                        detailReview.getReviewItemList().get(0).getActionItem(),
//                        detailReview.getReviewItemList().get(0).getMetric(),
//                        detailReview.getReviewItemList().get(0).getDetailPlan(),
//                        detailReview.getReviewItemList().get(0).getWeight(),
//                        "2023년 1분기 신규 모듈 개발 완료",
//                        1,
//                        95,
//                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
//                        3,
//                        75,
//                        detailReview.getId()
//        ));
//
//        int reviewId = detailReview.getId();
//
//        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
//                reviewId,
//                performanceReviewItemList
//        );
//
//        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusInProgress(requestPerformanceReviewVO);
//        System.out.println(modifyPerformanceReviewDTO);
//
//        assertNotNull(modifyPerformanceReviewDTO);
//    }
//
//    @DisplayName("업적 평가 상신")
//    @Test
//    @Transactional
//    public void modifyPerformanceReviewStatusSubmit(){
//
//        PerformanceReviewGoalDTO performanceReviewGoalDTO = new PerformanceReviewGoalDTO(
//                2025,
//                "2025 인사평가",
//                "A",
//                4,
//                "2025-03-01 08:00:00",
//                3
//        );
//
//        PerformanceReviewGoalDTO saveGoal = performanceReviewGoalService.addNewGoal(performanceReviewGoalDTO);
//        int id = saveGoal.getId();
//
//        PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO = new PerformanceReviewGoalItemDTO(
//                null,
//                "웹 애플리케이션 보안 강화",
//                "웹 애플리케이션의 보안 취약점을 보완하기 위한 작업",
//                "해결된 보안 취약점 수",
//                100,
//                "매월 보안 취약점 스캔",
//                null,
//                id
//        );
//
//        performanceReviewGoalItemService.addGoalItem(performanceReviewGoalItemDTO);
//
//        PerformanceReviewDTO performanceReviewDTO = new PerformanceReviewDTO(
//                "M",
//                2025,
//                "2025 인사평가",
//                4
//        );
//
//        PerformanceReviewDTO addReview = performanceReviewService.addNewPerformanceReview(performanceReviewDTO);
//
//        System.out.println("평가조회"+addReview);
//
//        DetailPerformanceReviewDTO detailReview = queryPerformanceReviewService.findDetailPerformanceReviewById(addReview.getId());
//
//        System.out.println("상세 조회" + detailReview);
//        System.out.println();
//        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
//        performanceReviewItemList.add(
//                new PerformanceReviewItemDTO( detailReview.getReviewItemList().get(0).getId(),
//                        detailReview.getReviewItemList().get(0).getGoal(),
//                        detailReview.getReviewItemList().get(0).getActionItem(),
//                        detailReview.getReviewItemList().get(0).getMetric(),
//                        detailReview.getReviewItemList().get(0).getDetailPlan(),
//                        detailReview.getReviewItemList().get(0).getWeight(),
//                        "2023년 1분기 신규 모듈 개발 완료",
//                        1,
//                        95,
//                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
//                        3,
//                        75,
//                        detailReview.getId()
//                ));
//
//        int reviewId = detailReview.getId();
//
//        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
//                reviewId,
//                performanceReviewItemList
//        );
//
//        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusSubmit(requestPerformanceReviewVO);
//        System.out.println(modifyPerformanceReviewDTO);
//
//        assertNotNull(modifyPerformanceReviewDTO);
//    }
//
//    @DisplayName("업적 평가 확인 중")
//    @Test
//    @Transactional
//    public void modifyPerformanceReviewStatusRead(){
//
//        PerformanceReviewGoalDTO performanceReviewGoalDTO = new PerformanceReviewGoalDTO(
//                2025,
//                "2025 인사평가",
//                "A",
//                4,
//                "2025-03-01 08:00:00",
//                3
//        );
//
//        PerformanceReviewGoalDTO saveGoal = performanceReviewGoalService.addNewGoal(performanceReviewGoalDTO);
//        int id = saveGoal.getId();
//
//        PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO = new PerformanceReviewGoalItemDTO(
//                null,
//                "웹 애플리케이션 보안 강화",
//                "웹 애플리케이션의 보안 취약점을 보완하기 위한 작업",
//                "해결된 보안 취약점 수",
//                100,
//                "매월 보안 취약점 스캔",
//                null,
//                id
//        );
//
//        performanceReviewGoalItemService.addGoalItem(performanceReviewGoalItemDTO);
//
//        PerformanceReviewDTO performanceReviewDTO = new PerformanceReviewDTO(
//                "M",
//                2025,
//                "2025 인사평가",
//                4
//        );
//
//        PerformanceReviewDTO addReview = performanceReviewService.addNewPerformanceReview(performanceReviewDTO);
//
//        System.out.println("평가조회"+addReview);
//
//        DetailPerformanceReviewDTO detailReview = queryPerformanceReviewService.findDetailPerformanceReviewById(addReview.getId());
//
//        System.out.println("상세 조회" + detailReview);
//        System.out.println();
//        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
//        performanceReviewItemList.add(
//                new PerformanceReviewItemDTO( detailReview.getReviewItemList().get(0).getId(),
//                        detailReview.getReviewItemList().get(0).getGoal(),
//                        detailReview.getReviewItemList().get(0).getActionItem(),
//                        detailReview.getReviewItemList().get(0).getMetric(),
//                        detailReview.getReviewItemList().get(0).getDetailPlan(),
//                        detailReview.getReviewItemList().get(0).getWeight(),
//                        "2023년 1분기 신규 모듈 개발 완료",
//                        1,
//                        95,
//                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
//                        3,
//                        75,
//                        detailReview.getId()
//                ));
//
//        int reviewId = detailReview.getId();
//
//        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
//                reviewId,
//                performanceReviewItemList
//        );
//
//        performanceReviewService.modifyPerformanceReviewStatusSubmit(requestPerformanceReviewVO);
//
//        List<PerformanceReviewItemDTO> newPerformanceReviewItemList = new ArrayList<>();
//        newPerformanceReviewItemList.add(
//                new PerformanceReviewItemDTO( detailReview.getReviewItemList().get(0).getId(),
//                        detailReview.getReviewItemList().get(0).getGoal(),
//                        detailReview.getReviewItemList().get(0).getActionItem(),
//                        detailReview.getReviewItemList().get(0).getMetric(),
//                        detailReview.getReviewItemList().get(0).getDetailPlan(),
//                        detailReview.getReviewItemList().get(0).getWeight(),
//                        "2023년 1분기 신규 모듈 개발 완료",
//                        1,
//                        95,
//                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
//                        2,
//                        85,
//                        detailReview.getId()
//                ));
//
//        RequestPerformanceReviewVO newRequestPerformanceReviewVO = new RequestPerformanceReviewVO(
//                reviewId,
//                newPerformanceReviewItemList
//        );
//
//        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusRead(newRequestPerformanceReviewVO);
//        System.out.println(modifyPerformanceReviewDTO);
//
//        assertNotNull(modifyPerformanceReviewDTO);
//    }
//
//    @DisplayName("중간 업적 평가 확정")
//    @Test
//    @Transactional
//    public void modifyPerformanceReviewStatusValid(){
//        PerformanceReviewGoalDTO performanceReviewGoalDTO = new PerformanceReviewGoalDTO(
//                2025,
//                "2025 인사평가",
//                "A",
//                4,
//                "2025-03-01 08:00:00",
//                3
//        );
//
//        PerformanceReviewGoalDTO saveGoal = performanceReviewGoalService.addNewGoal(performanceReviewGoalDTO);
//        int id = saveGoal.getId();
//
//        PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO = new PerformanceReviewGoalItemDTO(
//                null,
//                "웹 애플리케이션 보안 강화",
//                "웹 애플리케이션의 보안 취약점을 보완하기 위한 작업",
//                "해결된 보안 취약점 수",
//                100,
//                "매월 보안 취약점 스캔",
//                null,
//                id
//        );
//
//        performanceReviewGoalItemService.addGoalItem(performanceReviewGoalItemDTO);
//
//        PerformanceReviewDTO performanceReviewDTO = new PerformanceReviewDTO(
//                "M",
//                2025,
//                "2025 인사평가",
//                4
//        );
//
//        PerformanceReviewDTO addReview = performanceReviewService.addNewPerformanceReview(performanceReviewDTO);
//
//        System.out.println("평가조회"+addReview);
//
//        DetailPerformanceReviewDTO detailReview = queryPerformanceReviewService.findDetailPerformanceReviewById(addReview.getId());
//
//        System.out.println("상세 조회" + detailReview);
//        System.out.println();
//        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
//        performanceReviewItemList.add(
//                new PerformanceReviewItemDTO( detailReview.getReviewItemList().get(0).getId(),
//                        detailReview.getReviewItemList().get(0).getGoal(),
//                        detailReview.getReviewItemList().get(0).getActionItem(),
//                        detailReview.getReviewItemList().get(0).getMetric(),
//                        detailReview.getReviewItemList().get(0).getDetailPlan(),
//                        detailReview.getReviewItemList().get(0).getWeight(),
//                        "2023년 1분기 신규 모듈 개발 완료",
//                        1,
//                        95,
//                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
//                        3,
//                        75,
//                        detailReview.getId()
//                ));
//
//        int reviewId = detailReview.getId();
//
//        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
//                reviewId,
//                performanceReviewItemList
//        );
//
//        performanceReviewService.modifyPerformanceReviewStatusSubmit(requestPerformanceReviewVO);
//
//        List<PerformanceReviewItemDTO> newPerformanceReviewItemList = new ArrayList<>();
//        newPerformanceReviewItemList.add(
//                new PerformanceReviewItemDTO( detailReview.getReviewItemList().get(0).getId(),
//                        detailReview.getReviewItemList().get(0).getGoal(),
//                        detailReview.getReviewItemList().get(0).getActionItem(),
//                        detailReview.getReviewItemList().get(0).getMetric(),
//                        detailReview.getReviewItemList().get(0).getDetailPlan(),
//                        detailReview.getReviewItemList().get(0).getWeight(),
//                        "2023년 1분기 신규 모듈 개발 완료",
//                        1,
//                        95,
//                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
//                        2,
//                        85,
//                        detailReview.getId()
//                ));
//
//        RequestPerformanceReviewVO newRequestPerformanceReviewVO = new RequestPerformanceReviewVO(
//                reviewId,
//                newPerformanceReviewItemList
//        );
//
//        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusValid(newRequestPerformanceReviewVO);
//        System.out.println(modifyPerformanceReviewDTO);
//
//        assertNotNull(modifyPerformanceReviewDTO);
//    }
//
//    @DisplayName("연말 평가 확정")
//    @Test
//    @Transactional
//    public void modifyPerformanceReviewStatusValid2(){
//
//        PerformanceReviewGoalDTO performanceReviewGoalDTO = new PerformanceReviewGoalDTO(
//                2025,
//                "2025 인사평가",
//                "A",
//                4,
//                "2025-03-01 08:00:00",
//                3
//        );
//
//        PerformanceReviewGoalDTO saveGoal = performanceReviewGoalService.addNewGoal(performanceReviewGoalDTO);
//        int id = saveGoal.getId();
//
//        PerformanceReviewGoalItemDTO performanceReviewGoalItemDTO = new PerformanceReviewGoalItemDTO(
//                null,
//                "웹 애플리케이션 보안 강화",
//                "웹 애플리케이션의 보안 취약점을 보완하기 위한 작업",
//                "해결된 보안 취약점 수",
//                100,
//                "매월 보안 취약점 스캔",
//                null,
//                id
//        );
//        performanceReviewGoalItemService.addGoalItem(performanceReviewGoalItemDTO);
//
//
//        PerformanceReviewDTO midPerformanceReviewDTO = new PerformanceReviewDTO(
//                "M",
//                2025,
//                "2025 인사평가",
//                4
//        );
//
//        PerformanceReviewDTO addReviewMid = performanceReviewService.addNewPerformanceReview(midPerformanceReviewDTO);
//
//        System.out.println("평가조회"+addReviewMid);
//
//        DetailPerformanceReviewDTO detailReviewMid = queryPerformanceReviewService.findDetailPerformanceReviewById(addReviewMid.getId());
//
//        System.out.println("상세 조회" + detailReviewMid);
//        System.out.println();
//        List<PerformanceReviewItemDTO> midPerformanceReviewItemList = new ArrayList<>();
//        midPerformanceReviewItemList.add(
//                new PerformanceReviewItemDTO(detailReviewMid.getReviewItemList().get(0).getId(),
//                        detailReviewMid.getReviewItemList().get(0).getGoal(),
//                        detailReviewMid.getReviewItemList().get(0).getActionItem(),
//                        detailReviewMid.getReviewItemList().get(0).getMetric(),
//                        detailReviewMid.getReviewItemList().get(0).getDetailPlan(),
//                        detailReviewMid.getReviewItemList().get(0).getWeight(),
//                        "2023년 1분기 신규 모듈 개발 완료",
//                        1,
//                        95,
//                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
//                        3,
//                        75,
//                        detailReviewMid.getId()
//                ));
//
//        int midReviewId = detailReviewMid.getId();
//
//        RequestPerformanceReviewVO requestMidPerformanceReviewVO = new RequestPerformanceReviewVO(
//                midReviewId,
//                midPerformanceReviewItemList
//        );
//
//        performanceReviewService.modifyPerformanceReviewStatusSubmit(requestMidPerformanceReviewVO);
//        performanceReviewService.modifyPerformanceReviewStatusValid(requestMidPerformanceReviewVO);
//
//        PerformanceReviewDTO performanceReviewDTO = new PerformanceReviewDTO(
//                "F",
//                2025,
//                "2025 인사평가",
//                4
//        );
//
//        PerformanceReviewDTO addReview = performanceReviewService.addNewPerformanceReview(performanceReviewDTO);
//
//        System.out.println("평가조회"+addReview);
//
//        DetailPerformanceReviewDTO detailReview = queryPerformanceReviewService.findDetailPerformanceReviewById(addReview.getId());
//
//        System.out.println("상세 조회" + detailReview);
//        System.out.println();
//        List<PerformanceReviewItemDTO> performanceReviewItemList = new ArrayList<>();
//        performanceReviewItemList.add(
//                new PerformanceReviewItemDTO( detailReview.getReviewItemList().get(0).getId(),
//                        detailReview.getReviewItemList().get(0).getGoal(),
//                        detailReview.getReviewItemList().get(0).getActionItem(),
//                        detailReview.getReviewItemList().get(0).getMetric(),
//                        detailReview.getReviewItemList().get(0).getDetailPlan(),
//                        detailReview.getReviewItemList().get(0).getWeight(),
//                        "2023년 1분기 신규 모듈 개발 완료",
//                        1,
//                        95,
//                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
//                        3,
//                        75,
//                        detailReview.getId()
//                ));
//
//        int reviewId = detailReview.getId();
//
//        RequestPerformanceReviewVO requestPerformanceReviewVO = new RequestPerformanceReviewVO(
//                reviewId,
//                performanceReviewItemList
//        );
//
//        performanceReviewService.modifyPerformanceReviewStatusSubmit(requestPerformanceReviewVO);
//
//        List<PerformanceReviewItemDTO> newPerformanceReviewItemList = new ArrayList<>();
//        newPerformanceReviewItemList.add(
//                new PerformanceReviewItemDTO( detailReview.getReviewItemList().get(0).getId(),
//                        detailReview.getReviewItemList().get(0).getGoal(),
//                        detailReview.getReviewItemList().get(0).getActionItem(),
//                        detailReview.getReviewItemList().get(0).getMetric(),
//                        detailReview.getReviewItemList().get(0).getDetailPlan(),
//                        detailReview.getReviewItemList().get(0).getWeight(),
//                        "2023년 1분기 신규 모듈 개발 완료",
//                        1,
//                        95,
//                        "수정 신규 모듈을 개발하는 데 성공적으로 기여했고, 예상보다 빠르게 완료하여 팀 전체의 성과를 높였습니다.",
//                        2,
//                        85,
//                        detailReview.getId()
//                ));
//
//        RequestPerformanceReviewVO newRequestPerformanceReviewVO = new RequestPerformanceReviewVO(
//                reviewId,
//                newPerformanceReviewItemList
//        );
//
//        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = performanceReviewService.modifyPerformanceReviewStatusValid(newRequestPerformanceReviewVO);
//        System.out.println(modifyPerformanceReviewDTO);
//
//
//        assertNotNull(modifyPerformanceReviewDTO);
//    }
//
//}