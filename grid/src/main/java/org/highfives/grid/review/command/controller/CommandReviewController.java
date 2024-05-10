package org.highfives.grid.review.command.controller;

import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.service.CommandReviewService;
import org.highfives.grid.review.command.vo.ResponseReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
public class CommandReviewController {

    private final CommandReviewService reviewService;

    @Autowired
    public CommandReviewController(CommandReviewService reviewService) {
        this.reviewService = reviewService;
    }

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



}
