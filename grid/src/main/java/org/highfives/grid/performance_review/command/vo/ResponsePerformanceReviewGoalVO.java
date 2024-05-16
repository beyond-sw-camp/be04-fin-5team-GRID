package org.highfives.grid.performance_review.command.vo;

import lombok.*;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponsePerformanceReviewGoalVO {

    private int statusCode;
    private String message;
    private String href;
    private PerformanceReviewGoalDTO goal;
}
