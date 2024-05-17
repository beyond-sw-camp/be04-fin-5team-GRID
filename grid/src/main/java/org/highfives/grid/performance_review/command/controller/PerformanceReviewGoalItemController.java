package org.highfives.grid.performance_review.command.controller;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;
import org.highfives.grid.performance_review.command.dto.RequestGoalItemDTO;
import org.highfives.grid.performance_review.command.service.PerformanceReviewGoalItemService;
import org.highfives.grid.performance_review.command.vo.RequestGoalItemListVO;
import org.highfives.grid.performance_review.command.vo.ResponseDelGoalItemVO;
import org.highfives.grid.performance_review.command.vo.ResponseGoalItemListVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController(value = "commandGoalItemController")
@RequestMapping("goal-item")
public class PerformanceReviewGoalItemController {

    private final PerformanceReviewGoalItemService performanceReviewGoalItemService;
    private final ModelMapper modelMapper;

    @Autowired
    public PerformanceReviewGoalItemController(PerformanceReviewGoalItemService performanceReviewGoalItemService, ModelMapper modelMapper) {
        this.performanceReviewGoalItemService = performanceReviewGoalItemService;
        this.modelMapper = modelMapper;
    }

    // 목표 항목 추가 수정을 PerformanceReviewGoal에서 하기
    @PostMapping
    public ResponseEntity<ResponseGoalItemListVO> addGoalItem(
            @RequestBody RequestGoalItemListVO requestGoalItemListVO) {
        System.out.println(requestGoalItemListVO);
        List<RequestGoalItemDTO> requsetList = requestGoalItemListVO.getGoalItemList();
        System.out.println(requsetList);

        List<PerformanceReviewGoalItemDTO> responseList = new ArrayList<>();
        // 목표 항목이 있으면
        if(!requsetList.isEmpty()){
            for (RequestGoalItemDTO request : requsetList) {
                // 목표 항목 변경
                PerformanceReviewGoalItemDTO item = modelMapper.map(request, PerformanceReviewGoalItemDTO.class);
                item.setGoalId(requestGoalItemListVO.getGoalId());

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
            ResponseGoalItemListVO response = ResponseGoalItemListVO.builder()
                    .statusCode(200)
                    .message("평가 항목 추가 완료")
                    .href("review-goal/detail/{goalId}")
                    .itemList(responseList)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            ResponseGoalItemListVO response = ResponseGoalItemListVO.builder()
                    .statusCode(404)
                    .message("변경할 리스트 없음")
                    .href("review-goal/detail/{goalId}")
                    .itemList(null)
                    .build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDelGoalItemVO> removeGoalItem(@PathVariable int id){
        int removeId = performanceReviewGoalItemService.removeGoalItem(id);

        ResponseDelGoalItemVO response = ResponseDelGoalItemVO.builder()
                .statusCode(200)
                .message("평가 항목 삭제 완료")
                .href("review-goal/detail/{goalId}")
                .id(removeId)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
