package org.highfives.grid.performance_review.command.vo;

import lombok.*;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponsePerformanceReviewGoalItemVO {

    private int statusCode;
    private String message;
    private String href;
    private PerformanceReviewGoalItemDTO item;
}
