package org.highfives.grid.performance_review.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PerformanceReviewDTO {
    private Integer id;
    private String type;
    private Integer year;
    private String reviewName;
    private String approvalStatus;
    private WriterDTO writer;
    private String writeTime;
    private ApproverDTO approver;
    private String approvalTime;
}
