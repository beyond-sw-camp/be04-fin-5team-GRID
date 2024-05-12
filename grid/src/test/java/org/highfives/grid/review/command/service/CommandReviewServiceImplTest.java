package org.highfives.grid.review.command.service;

import org.assertj.core.api.Assertions;
import org.highfives.grid.department.command.dto.DepartmentDTO;
import org.highfives.grid.review.command.dto.ReviewDTO;
import org.highfives.grid.review.command.dto.ReviewHistoryDTO;
import org.highfives.grid.review.command.dto.ReviewListDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommandReviewServiceImplTest {


    private final CommandReviewService commandReviewService;

    @Autowired
    public CommandReviewServiceImplTest(CommandReviewService commandReviewService) {
        this.commandReviewService = commandReviewService;
    }

    @Test
    @DisplayName("작성한 평가 조회")
    void findReviewById() {

        // Given

        int id = 1;

        // When
        ReviewDTO reviewById = commandReviewService.findReviewById(id);


        // Then

        assertThat(reviewById.getReviewId()).isEqualTo(id);


    }

    @Test
    @DisplayName("평가 항목 리스트 전체 조회")
    void findAllReview() {

        // Given

        // When

        ReviewListDTO reviewList = commandReviewService.findAllReview();


        // Then
        assertThat(reviewList).isNotNull();

    }

    @Test
    @DisplayName("평가 항목 내역 단일 조회")
    void findReviewHistoryById() {

        // Given

        int id = 1;

        // When

        ReviewHistoryDTO reviewHistoryDTO = commandReviewService.findReviewHistoryById(id);


        // Then
        assertThat(reviewHistoryDTO.getReviewerId()).isSameAs(id);

    }
}