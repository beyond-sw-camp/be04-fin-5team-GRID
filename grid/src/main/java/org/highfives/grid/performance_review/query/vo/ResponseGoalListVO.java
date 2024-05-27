package org.highfives.grid.performance_review.query.vo;

import lombok.*;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewGoalDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponseGoalListVO {
    private int statusCode;
    private String message;
    private String href;
    private List<PerformanceReviewGoalDTO> findGoalList;
}
