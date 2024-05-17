package org.highfives.grid.performance_review.query.vo;

import lombok.*;
import org.highfives.grid.performance_review.query.dto.DetailPerformanceReviewDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponseDetailReviewVO {
    private int statusCode;
    private String message;
    private String href;
    private DetailPerformanceReviewDTO findDetailReview;
}
