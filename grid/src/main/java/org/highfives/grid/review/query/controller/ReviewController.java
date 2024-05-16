package org.highfives.grid.review.query.controller;

import org.highfives.grid.review.query.dto.ReviewHistoryDTO;
import org.highfives.grid.review.query.service.ReviewService;
import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;
import org.highfives.grid.review.query.vo.ResponseReviewHistoryAndScoreVO;
import org.highfives.grid.review.query.vo.ResponseReviewHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "QueryReviewController")
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /* 설명. 동료 평가 결과 조회 */
    @GetMapping("history-score/{historyId}/{revieweeId}")
    public ResponseEntity<ResponseReviewHistoryAndScoreVO> findHistoryAndScoreById(@PathVariable int historyId,
                                                                                   @PathVariable int revieweeId) {

        List<ReviewHistoryAndScoreDTO> reviewHistoryAndScoreDTO = reviewService.findHistoryAndScoreById(historyId,revieweeId);

        ResponseReviewHistoryAndScoreVO responseReviewHistoryAndScoreVO = ResponseReviewHistoryAndScoreVO.builder()
                .href("/review/history-score/{history_id}/{revieweeId}")
                .statusCode(200)
                .message("SUCCESS")
                .result(reviewHistoryAndScoreDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewHistoryAndScoreVO);
    }

    /* 설명. 배정된 동료 평가 조회 */
    @GetMapping("assigned-review/{reviewerId}")
    public ResponseEntity<ResponseReviewHistoryVO> findAssignedReviewByReviewerId(@PathVariable int reviewerId) {

        List<ReviewHistoryDTO> assignedReviewList = reviewService.findAssignedReviewByReviewerId(reviewerId);


        ResponseReviewHistoryVO responseReviewHistoryVO = ResponseReviewHistoryVO.builder()
                .href("/review/assigned-review/{reviewerId}")
                .statusCode(200)
                .message("SUCCESS")
                .result(assignedReviewList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewHistoryVO);
    }




}
