package org.highfives.grid.performance_review.command.vo;

import lombok.*;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponseGoalAndItemVO {
    private int statusCode;
    private String message;
    private String href;
    private PerformanceReviewGoalDTO goal;
    private List<PerformanceReviewGoalItemDTO> itemList;
}
