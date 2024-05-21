package org.highfives.grid.performance_review.command.vo;

import lombok.*;

import org.highfives.grid.performance_review.command.dto.PerformanceReviewItemDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class RequestPerformanceReviewVO {
    // 평가 저장
    private int reviewId;
    private List<PerformanceReviewItemDTO> performanceReviewItemList;
}
