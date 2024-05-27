package org.highfives.grid.performance_review.query.vo;

import lombok.*;
import org.highfives.grid.performance_review.query.dto.TotalPerformanceReviewDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ResponseTotalPerformanceReviewVO {
    private int statusCode;
    private String message;
    private String href;
    private List<TotalPerformanceReviewDTO> findTotalList;
}
