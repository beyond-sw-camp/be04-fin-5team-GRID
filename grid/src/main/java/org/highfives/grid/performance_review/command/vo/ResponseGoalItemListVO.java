package org.highfives.grid.performance_review.command.vo;

import lombok.*;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewGoalItemDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponseGoalItemListVO {
    private int statusCode;
    private String message;
    private String href;
    private List<PerformanceReviewGoalItemDTO> itemList;
}
