package org.highfives.grid.performance_review.command.vo;

import lombok.*;
import org.highfives.grid.performance_review.command.dto.ModifyPerformanceReviewDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponseModifyPerformanceReviewVO {
    private int statusCode;
    private String message;
    private String href;
    private ModifyPerformanceReviewDTO modifyPerformanceReviewDTO;
}
