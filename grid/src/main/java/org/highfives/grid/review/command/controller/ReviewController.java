package org.highfives.grid.review.command.controller;

import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;
import org.highfives.grid.review.command.service.ReviewService;
import org.highfives.grid.review.command.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "CommandReviewController")
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /* 설명. 평가 조회 기능(Review) */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseReviewVO> findReviewById(@PathVariable int id) {

        ReviewDTO reviewDTO = reviewService.findReviewById(id);

        ResponseReviewVO responseReviewVO = ResponseReviewVO.builder()
                .message("find success")
                .statusCode(200)
                .href("/review/{id}")
                .result(reviewDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewVO);

    }

    /* 설명. 평가 후 결과 저장 기능 (Review) */
    @PostMapping
    public ResponseEntity<ResponseAddReviewVO> addReview(@RequestBody List<ReviewDTO> requestData) {

         List<ReviewDTO> reviewInfo = reviewService.addReview(requestData);

        ResponseAddReviewVO responseReviewVO = ResponseAddReviewVO.builder()
                .message("create success")
                .statusCode(201)
                .href("/review")
                .result(reviewInfo)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseReviewVO);
    }
    /* 설명. 평가 수정 기능 (Review, 점수 변경 있을 경우) */
    @PutMapping
    public ResponseEntity<ResponseReviewVO> modifyReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO reviewInfo = reviewService.modifyReview(reviewDTO);

        ResponseReviewVO responseReviewVO = ResponseReviewVO.builder()
                .message("update success")
                .statusCode(200)
                .href("/review")
                .result(reviewInfo)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewVO);
    }

// ReviewList

    /* 설명. 평가 항목 리스트 전체 조회 (ReviewList) */
    @GetMapping("/list")
    public ResponseEntity<ResponseReviewListVO> findAllReview() {
        List<ReviewListDTO> reviewList = reviewService.findAllReview();

        ResponseReviewListVO responseReviewListVO = ResponseReviewListVO.builder()
                .message("find success")
                .statusCode(200)
                .href("/review/list")
                .result(reviewList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewListVO);

    }

    /* 설명. 평가 항목 추가 기능 (ReviewList) */
    @PostMapping("/list")
    public ResponseEntity<ResponseAddReviewListVO> addReviewList(@RequestBody ReviewListDTO reviewListDTO) {
        ReviewListDTO addReviewListInfo = reviewService.addReviewList(reviewListDTO);

        ResponseAddReviewListVO responseAddReviewListVO = ResponseAddReviewListVO.builder()
                .message("create success")
                .statusCode(201)
                .href("/review/list")
                .result(addReviewListInfo)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseAddReviewListVO);
    }

    /* 설명. 평가 항목 수정 기능 (ReviewList) */
    @PutMapping("/list")
    public ResponseEntity<ResponseAddReviewListVO> modifyReviewList(@RequestBody ReviewListDTO reviewListDTO) {
        ReviewListDTO modifyReviewListInfo = reviewService.modifyReviewList(reviewListDTO);

        ResponseAddReviewListVO responseAddReviewListVO = ResponseAddReviewListVO.builder()
                .message("update success")
                .statusCode(200)
                .href("/review/list")
                .result(modifyReviewListInfo)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseAddReviewListVO);
    }

    /* 설명. 평가 항목 삭제 기능 (ReviewList) */
    @DeleteMapping("/list/{id}")
    public ResponseEntity<ResponseAddReviewListVO> deleteReviewList(@PathVariable int id) {
        reviewService.deleteReviewList(id);

        ResponseAddReviewListVO responseAddReviewListVO = ResponseAddReviewListVO.builder()
                .message("delete success")
                .statusCode(200)
                .href("/review/list")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseAddReviewListVO);
    }

// ReviewHistory
    /* 설명. 평가 항목 내역 단일 조회 (ReviewHistory) */
    @GetMapping("/history/{id}")
    public ResponseEntity<ResponseReviewHistoryVO> findReviewHistoryById(@PathVariable int id) {

        ReviewHistoryDTO responseData = reviewService.findReviewHistoryById(id);

        ResponseReviewHistoryVO responseReviewHistoryVO = ResponseReviewHistoryVO.builder()
                .message("find success")
                .statusCode(200)
                .href("/review/history/{id}")
                .result(responseData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewHistoryVO);

    }

    /* 설명. 평가 작성 시 평가 내역 추가 기능 (ReviewHistory) */
    @PostMapping("/history")
    public ResponseEntity<ResponseAddReviewHistoryVO> addReviewHistory(@RequestBody ReviewHistoryDTO historyDTO) {

        List<ReviewHistoryDTO> responseData = reviewService.addReviewHistory(historyDTO);

        ResponseAddReviewHistoryVO responseReviewHistoryVO = ResponseAddReviewHistoryVO.builder()
                .message("create success")
                .statusCode(201)
                .href("/review/history")
                .result(responseData)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseReviewHistoryVO);
    }

    /* 설명. 평가 내역 수정 (ReviewHistory) */
    @PutMapping("/history")
    public ResponseEntity<ResponseReviewHistoryVO> modifyReviewHistory(@RequestBody ReviewHistoryDTO historyDTO) {

        ReviewHistoryDTO responseData = reviewService.modifyReviewHistory(historyDTO);

        ResponseReviewHistoryVO responseReviewHistoryVO = ResponseReviewHistoryVO.builder()
                .message("update success")
                .statusCode(200)
                .href("/review/history")
                .result(responseData)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewHistoryVO);
    }

    /* 설명. 평가 항목 삭제 기능 (ReviewHistory) */
    @DeleteMapping("/history")
    public ResponseEntity<ResponseReviewHistoryVO> deleteReviewHistory(@RequestBody int id) {

        reviewService.deleteReviewHistory(id);

        ResponseReviewHistoryVO responseReviewHistoryVO = ResponseReviewHistoryVO.builder()
                .message("delete success")
                .statusCode(200)
                .href("/review/history")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewHistoryVO);
    }

}
