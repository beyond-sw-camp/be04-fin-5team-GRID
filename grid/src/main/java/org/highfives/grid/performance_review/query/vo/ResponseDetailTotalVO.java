package org.highfives.grid.performance_review.query.vo;

import lombok.*;
import org.highfives.grid.performance_review.query.dto.TotalPerformanceReviewDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponseDetailTotalVO {
    private int statusCode;
    private String message;
    private String href;
    private TotalPerformanceReviewDTO findTotal;
}
