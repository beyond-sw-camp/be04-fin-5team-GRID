package org.highfives.grid.performance_review.command.controller;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;
import org.highfives.grid.performance_review.command.dto.RequestGoalItemDTO;
import org.highfives.grid.performance_review.command.service.PerformanceReviewGoalItemService;
import org.highfives.grid.performance_review.command.service.PerformanceReviewGoalService;
import org.highfives.grid.performance_review.command.vo.RequestGoalVO;
import org.highfives.grid.performance_review.command.vo.ResponseGoalAndItemVO;
import org.highfives.grid.performance_review.command.vo.ResponsePerformanceReviewGoalVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController(value = "commandGoalController")
@RequestMapping("review-goal")
public class PerformanceReviewGoalController {

    private final PerformanceReviewGoalService performanceReviewGoalService;
    private final PerformanceReviewGoalItemService performanceReviewGoalItemService;
    private final ModelMapper modelMapper;

    @Autowired
    public PerformanceReviewGoalController(PerformanceReviewGoalService performanceReviewGoalService, PerformanceReviewGoalItemService performanceReviewGoalItemService, ModelMapper modelMapper) {
        this.performanceReviewGoalService = performanceReviewGoalService;
        this.performanceReviewGoalItemService = performanceReviewGoalItemService;
        this.modelMapper = modelMapper;
    }

    // 업적 평가 목표 처음 생성
    @PostMapping
    public ResponseEntity<ResponsePerformanceReviewGoalVO> addGoal(
            @RequestBody PerformanceReviewGoalDTO performanceReviewGoalDTO){

        PerformanceReviewGoalDTO addGoalDTO = performanceReviewGoalService.addNewGoal(performanceReviewGoalDTO);
        ResponsePerformanceReviewGoalVO response = ResponsePerformanceReviewGoalVO.builder()
                .statusCode(200)
                .message("평가 추가 완료")
                .href("review-goal/detail/{id}")
                .goal(addGoalDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 업적 평가 목표 작성 중 상태로 변경
    @PutMapping("/in-progress")
    public ResponseEntity<ResponseGoalAndItemVO> modifyGoalStatusInProgress(
            @RequestBody RequestGoalVO requestGoalVO) {

        try{

            // 목표 상태 작성 중으로 수정
            PerformanceReviewGoalDTO goal = performanceReviewGoalService.modifyGoalStatusInProgress(requestGoalVO.getId());

            // 목표 항목 변경
            List<RequestGoalItemDTO> requsetList = requestGoalVO.getGoalItemList();

            List<PerformanceReviewGoalItemDTO> responseList = new ArrayList<>();
            // 목표 항목이 있으면
            if(!requsetList.isEmpty()){
                for (RequestGoalItemDTO request : requsetList) {
                    // 목표 항목 변경
                    PerformanceReviewGoalItemDTO item = modelMapper.map(request, PerformanceReviewGoalItemDTO.class);
                    item.setGoalId(requestGoalVO.getId());

                    //목표 항목에 id 없으면 추가, 있으면 수정
                    PerformanceReviewGoalItemDTO resItem = new PerformanceReviewGoalItemDTO();
                    if(item.getId() == null){
                        resItem = performanceReviewGoalItemService.addGoalItem(item);
                    } else{
                        resItem = performanceReviewGoalItemService.modifyGoalItem(item);
                    }
                    System.out.println(resItem);
                    responseList.add(resItem);
                }

                ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                        .statusCode(200)
                        .message("평가 목표 작성 중 수정 완료")
                        .href("review-goal/detail/{goalId}")
                        .goal(goal)
                        .itemList(responseList)
                        .build();

                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                        .statusCode(404)
                        .message("변경할 리스트 없음")
                        .href("review-goal/detail/{goalId}")
                        .goal(goal)
                        .itemList(null)
                        .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            // 예외 처리
            ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .href("performance-review/goal/detail/{goalId}")
                    .goal(null)
                    .itemList(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
        }
    }

    // 업적 평가 목표 상신 상태로 변경
    @PutMapping("/submit")
    public ResponseEntity<ResponseGoalAndItemVO> modifyGoalStatusSubmit(
            @RequestBody RequestGoalVO requestGoalVO) {

        try{

            // 목표 상태 상신으로 수정
            PerformanceReviewGoalDTO goal = performanceReviewGoalService.modifyGoalStatusSubmit(requestGoalVO.getId());

            // 목표 항목 변경
            List<RequestGoalItemDTO> requsetList = requestGoalVO.getGoalItemList();

            List<PerformanceReviewGoalItemDTO> responseList = new ArrayList<>();
            // 목표 항목이 있으면
            if(!requsetList.isEmpty()){
                for (RequestGoalItemDTO request : requsetList) {
                    // 목표 항목 변경
                    PerformanceReviewGoalItemDTO item = modelMapper.map(request, PerformanceReviewGoalItemDTO.class);
                    item.setGoalId(requestGoalVO.getId());

                    //목표 항목에 id 없으면 추가, 있으면 수정
                    PerformanceReviewGoalItemDTO resItem = new PerformanceReviewGoalItemDTO();
                    if(item.getId() == null){
                        resItem = performanceReviewGoalItemService.addGoalItem(item);
                    } else{
                        resItem = performanceReviewGoalItemService.modifyGoalItem(item);
                    }
                    System.out.println(resItem);
                    responseList.add(resItem);
                }

                ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                        .statusCode(200)
                        .message("평가 목표 상신 수정 완료")
                        .href("performance-review/goal/detail/{goalId}")
                        .goal(goal)
                        .itemList(responseList)
                        .build();

                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                        .statusCode(404)
                        .message("변경할 리스트 없음")
                        .href("performance-review/goal/detail/{goalId}")
                        .goal(goal)
                        .itemList(null)
                        .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            // 예외 처리
            ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .href("performance-review/goal/detail/{goalId}")
                    .goal(null)
                    .itemList(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
        }
    }

    // 업적 평가 목표 확인중
    @PutMapping("/read")
    public ResponseEntity<ResponseGoalAndItemVO> modifyGoalStatusRead(
            @RequestBody RequestGoalVO requestGoalVO) {

        try{

            System.out.println("리퀘스트"+requestGoalVO);
            // 목표 상태 확인 중으로 수정
            PerformanceReviewGoalDTO goal = performanceReviewGoalService.modifyGoalStatusRead(requestGoalVO.getId());

            // 목표 항목 변경
            List<RequestGoalItemDTO> requsetList = requestGoalVO.getGoalItemList();

            List<PerformanceReviewGoalItemDTO> responseList = new ArrayList<>();
            // 목표 항목이 있으면
            if(!requsetList.isEmpty()){
                for (RequestGoalItemDTO request : requsetList) {
                    // 목표 항목 변경
                    PerformanceReviewGoalItemDTO item = modelMapper.map(request, PerformanceReviewGoalItemDTO.class);
                    item.setGoalId(requestGoalVO.getId());

                    //목표 항목 수정
                    PerformanceReviewGoalItemDTO  resItem = performanceReviewGoalItemService.modifyGoalItem(item);

                    System.out.println(resItem);
                    responseList.add(resItem);
                }

                ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                        .statusCode(200)
                        .message("평가 목표 확인 중 수정 완료")
                        .href("performance-review/goal/detail/{goalId}")
                        .goal(goal)
                        .itemList(responseList)
                        .build();

                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                        .statusCode(404)
                        .message("변경할 리스트 없음")
                        .href("performance-review/goal/detail/{goalId}")
                        .goal(goal)
                        .itemList(null)
                        .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            // 예외 처리
            ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .href("performance-review/goal/detail/{goalId}")
                    .goal(null)
                    .itemList(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
        }
    }

    // 승인
    @PutMapping("/approval")
    public ResponseEntity<ResponseGoalAndItemVO> modifyGoalStatusApproval(
            @RequestBody RequestGoalVO requestGoalVO) {

        try{

            // 목표 상태 승인으로 수정
            PerformanceReviewGoalDTO goal = performanceReviewGoalService.modifyGoalStatusApproval(requestGoalVO.getId());

            List<RequestGoalItemDTO> requestList = requestGoalVO.getGoalItemList();

            List<PerformanceReviewGoalItemDTO> responseList = new ArrayList<>();

            // 목표 항목이 있으면
            if(!requestList.isEmpty()) {
                for (RequestGoalItemDTO request : requestList) {
                    PerformanceReviewGoalItemDTO item = modelMapper.map(request, PerformanceReviewGoalItemDTO.class);
                    responseList.add(item);
                }
            }
            ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                    .statusCode(200)
                    .message("평가 목표 승인 수정 완료")
                    .href("performance-review/goal")
                    .goal(goal)
                    .itemList(responseList)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            // 예외 처리
            ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .href("performance-review/goal")
                    .goal(null)
                    .itemList(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
        }
    }


    // 반려
    @PutMapping("/denied")
    public ResponseEntity<ResponseGoalAndItemVO> modifyGoalStatusDenied(
            @RequestBody RequestGoalVO requestGoalVO) {

        try{

            // 목표 상태 확인 중으로 수정
            PerformanceReviewGoalDTO goal = performanceReviewGoalService.modifyGoalStatusDenied(requestGoalVO.getId());

            // 목표 항목 변경
            List<RequestGoalItemDTO> requsetList = requestGoalVO.getGoalItemList();

            List<PerformanceReviewGoalItemDTO> responseList = new ArrayList<>();
            // 목표 항목이 있으면
            if(!requsetList.isEmpty()){
                for (RequestGoalItemDTO request : requsetList) {
                    // 목표 항목 변경
                    PerformanceReviewGoalItemDTO item = modelMapper.map(request, PerformanceReviewGoalItemDTO.class);
                    item.setGoalId(requestGoalVO.getId());

                    //목표 항목 수정
                    PerformanceReviewGoalItemDTO  resItem = performanceReviewGoalItemService.modifyGoalItem(item);

                    System.out.println(resItem);
                    responseList.add(resItem);
                }

                ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                        .statusCode(200)
                        .message("평가 목표 반려 수정 완료")
                        .href("performance-review/goal")
                        .goal(goal)
                        .itemList(responseList)
                        .build();

                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                        .statusCode(404)
                        .message("변경할 리스트 없음")
                        .href("performance-review/goal")
                        .goal(goal)
                        .itemList(null)
                        .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            // 예외 처리
            ResponseGoalAndItemVO response = ResponseGoalAndItemVO.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .href("performance-review/goal")
                    .goal(null)
                    .itemList(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
        }
    }
}
