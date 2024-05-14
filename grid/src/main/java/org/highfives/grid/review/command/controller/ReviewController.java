package org.highfives.grid.review.command.controller;

import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;
import org.highfives.grid.review.command.service.ReviewService;
import org.highfives.grid.review.command.vo.ResponseReviewHistoryVO;
import org.highfives.grid.review.command.vo.ResponseReviewListVO;
import org.highfives.grid.review.command.vo.ResponseReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "Command")
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /* 설명. 평가 결과 조회 기능 */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseReviewVO> findReviewById(@PathVariable int id) {

        ReviewDTO reviewDTO = reviewService.findReviewById(id);

        ResponseReviewVO responseReviewVO = ResponseReviewVO.builder()
                .message("success")
                .statusCode(200)
                .href("/{id}")
                .result(reviewDTO)
                .build();


        return ResponseEntity.status(HttpStatus.OK).body(responseReviewVO);



    }

    /* 설명. 평가 후 결과 저장 기능 */
    @PostMapping
    public ResponseEntity<ResponseReviewVO> addReview(@RequestBody ReviewDTO requestData) {


         ReviewDTO reviewInfo = reviewService.addReview(requestData);


        ResponseReviewVO responseReviewVO = ResponseReviewVO.builder()
                .message("success")
                .statusCode(201)
                .href("/{id}")
                .result(reviewInfo)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseReviewVO);
    }

    /* 설명. 평가 항목 리스트 전체 조회*/
    @GetMapping("/list")
    public ResponseEntity<ResponseReviewListVO> findAllReview() {
        ReviewListDTO reviewList = reviewService.findAllReview();

        ResponseReviewListVO responseReviewListVO = ResponseReviewListVO.builder()
                .message("success")
                .statusCode(200)
                .href("/{id}")
                .result(reviewList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewListVO);

    }

    /* 설명. 평가 항목 내역 단일 조회 */
    @GetMapping("/history/{id}")
    public ResponseEntity<ResponseReviewHistoryVO> findReviewHistoryById(@PathVariable int id) {

        ReviewHistoryDTO responseData = reviewService.findReviewHistoryById(id);

        ResponseReviewHistoryVO responseReviewHistoryVO = ResponseReviewHistoryVO.builder()
                .message("success")
                .statusCode(200)
                .href("/{id}")
                .result(responseData)
                .build();


        return ResponseEntity.status(HttpStatus.OK).body(responseReviewHistoryVO);

    }

    /* 설명. 평가 작성 시 평가 내역 추가 기능 */
    @PostMapping("/history")
    public ResponseEntity<ResponseReviewHistoryVO> addReviewHistory(@RequestBody ReviewHistoryDTO historyDTO) {

        ReviewHistoryDTO responseData = reviewService.addReviewHistory(historyDTO);

        ResponseReviewHistoryVO responseReviewHistoryVO = ResponseReviewHistoryVO.builder()
                .message("success")
                .statusCode(200)
                .href("/{id}")
                .result(responseData)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseReviewHistoryVO);
    }





}