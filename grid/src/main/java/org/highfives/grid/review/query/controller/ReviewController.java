package org.highfives.grid.review.query.controller;

import org.highfives.grid.review.query.vo.ResponseReviewListVO;
import org.highfives.grid.review.query.dto.ReviewHistoryDTO;
import org.highfives.grid.review.query.dto.ReviewListDTO;
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
    @GetMapping("/history-score/{historyId}/{revieweeId}")
    public ResponseEntity<ResponseReviewHistoryAndScoreVO> findHistoryAndScoreById(@PathVariable int historyId,
                                                                                   @PathVariable int revieweeId) {

        List<ReviewHistoryAndScoreDTO> reviewHistoryAndScoreDTO = reviewService.findHistoryAndScoreById(historyId, revieweeId);

        ResponseReviewHistoryAndScoreVO responseReviewHistoryAndScoreVO = ResponseReviewHistoryAndScoreVO.builder()
                .href("/review/history-score/{history_id}/{revieweeId}")
                .statusCode(200)
                .message("SUCCESS")
                .result(reviewHistoryAndScoreDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewHistoryAndScoreVO);
    }

    /* 설명. 배정된 동료 평가 조회 */
    @GetMapping("/assigned-review/{reviewerId}")
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

    @GetMapping("/history-list")
    public ResponseEntity<ResponseReviewHistoryVO> findHistoryList() {

        List<ReviewHistoryDTO> ReviewList = reviewService.findHistoryList();

        ResponseReviewHistoryVO responseReviewHistoryVO = ResponseReviewHistoryVO.builder()
                .href("/review/history-list")
                .statusCode(200)
                .message("SUCCESS")
                .result(ReviewList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewHistoryVO);

    }

    /* 설명. 자신의 동료 평가 조회 */
    @GetMapping("/my-review/{revieweeId}")
    public ResponseEntity<ResponseReviewHistoryVO> findMyReviewHistory(@PathVariable int revieweeId) {

        List<ReviewHistoryDTO> ReviewList = reviewService.findMyReviewHistory(revieweeId);

        ResponseReviewHistoryVO responseReviewHistoryVO = ResponseReviewHistoryVO.builder()
                .href("/my-review/{revieweeId}")
                .statusCode(200)
                .message("SUCCESS")
                .result(ReviewList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewHistoryVO);

    }

    @GetMapping("/list/{id}")
    public ResponseEntity<ResponseReviewListVO> findReviewList(@PathVariable int id) {

        ReviewListDTO reviewListDTO = reviewService.findReviewList(id);

        ResponseReviewListVO responseReviewListVO = ResponseReviewListVO.builder()
                .href("/list/{id}")
                .statusCode(200)
                .message("SUCCESS")
                .result(reviewListDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseReviewListVO);
    }
}
