package org.highfives.grid.performance_review.query.controller;

import org.highfives.grid.performance_review.query.dto.DetailGoalDTO;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.performance_review.query.service.PerformanceReviewGoalService;
import org.highfives.grid.performance_review.query.vo.ResponseDetailGoalVO;
import org.highfives.grid.performance_review.query.vo.ResponseGoalListVO;
import org.highfives.grid.performance_review.query.vo.ResponseGoalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "QueryGoalController")
@RequestMapping("review-goal")
public class PerformanceReviewGoalController {

    private final PerformanceReviewGoalService performanceReviewGoalService;

    @Autowired
    public PerformanceReviewGoalController(PerformanceReviewGoalService performanceReviewGoalService) {
        this.performanceReviewGoalService = performanceReviewGoalService;
    }

    // 한 팀원의 작성한 평가 목표 목록 조회
    @GetMapping("/member/{employeeId}")
    public ResponseEntity<ResponseGoalListVO> findAllGoalByWriterId(@PathVariable int employeeId){
        List<PerformanceReviewGoalDTO> findGoalList = performanceReviewGoalService.findAllGoalByWriterId(employeeId);

        ResponseGoalListVO response =
                new ResponseGoalListVO().builder()
                        .statusCode(200)
                        .message("평가 목록 조회 성공")
                        .findGoalList(findGoalList)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 한 팀장의 작성한 평가 목표 목록 조회
    @GetMapping("/leader/{employeeId}")
    public ResponseEntity<ResponseGoalListVO> findAllGoalByApproverId(@PathVariable int employeeId){
        List<PerformanceReviewGoalDTO> findGoalList = performanceReviewGoalService.findAllGoalByApproverId(employeeId);

        ResponseGoalListVO response =
                new ResponseGoalListVO().builder()
                        .statusCode(200)
                        .message("평가 목록 조회 성공")
                        .findGoalList(findGoalList)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 목표 하나 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponseDetailGoalVO> findDetailGoalById(@PathVariable int id){
        DetailGoalDTO findDetailGoal = performanceReviewGoalService.findDetailGoalById(id);

        new ResponseDetailGoalVO();
        ResponseDetailGoalVO response =
                ResponseDetailGoalVO.builder()
                        .statusCode(200)
                        .message("평가 상세 조회 성공")
                        .findDetailGoal(findDetailGoal)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 올해 작성한 목표 조회
    @GetMapping("/{year}/{employeeId}")
    public ResponseEntity<ResponseGoalVO> findGoalByYearAndEmployeeId(
            @PathVariable int year,
            @PathVariable int employeeId){

        PerformanceReviewGoalDTO performanceReviewGoalDTO = performanceReviewGoalService.findGoalByWriterIdAndYear(employeeId, year);
        System.out.println(performanceReviewGoalDTO);

        new ResponseGoalVO();
        ResponseGoalVO response =
                ResponseGoalVO.builder()
                        .statusCode(200)
                        .message("평가 상세 조회 성공")
                        .findGoal(performanceReviewGoalDTO)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
