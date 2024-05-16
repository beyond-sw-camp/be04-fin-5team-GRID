package org.highfives.grid.review.query.controller;

import org.highfives.grid.review.query.service.ReviewService;
import org.highfives.grid.review.query.dto.ReviewHistoryAndScoreDTO;
import org.highfives.grid.review.query.vo.ResponseReviewHistoryAndScoreVO;
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

    @GetMapping("history-score/{historyId}")
    public ResponseEntity<ResponseReviewHistoryAndScoreVO> findHistoryAndScoreById(@PathVariable int historyId) {

        List<ReviewHistoryAndScoreDTO> reviewHistoryAndScoreDTO = reviewService.findHistoryAndScoreById(historyId);

        ResponseReviewHistoryAndScoreVO responseReviewHistoryAndScoreVO = ResponseReviewHistoryAndScoreVO.builder()
                .href("/review/history-score/{history_id}")
                .statusCode(200)
                .message("SUCCESS")
                .result(reviewHistoryAndScoreDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewHistoryAndScoreVO);
    }


}
