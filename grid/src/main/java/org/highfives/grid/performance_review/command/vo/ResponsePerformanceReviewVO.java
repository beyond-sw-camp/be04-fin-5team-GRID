package org.highfives.grid.performance_review.command.vo;

import lombok.*;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponsePerformanceReviewVO {

    private int statusCode;
    private String message;
    private String href;
    private PerformanceReviewDTO performanceReview;
}
