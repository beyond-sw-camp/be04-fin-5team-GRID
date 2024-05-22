package org.highfives.grid.performance_review.command.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ModifyPerformanceReviewDTO {
    private PerformanceReviewDTO performanceReviewDTO;
    private List<PerformanceReviewItemDTO> performanceReviewItemDTO;
}
