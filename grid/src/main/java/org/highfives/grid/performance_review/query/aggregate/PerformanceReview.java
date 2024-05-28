package org.highfives.grid.performance_review.query.aggregate;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PerformanceReview {
    private Integer id;
    private String type;
    private Integer year;
    private String reviewName;
    private String approvalStatus;
    private Integer writerId;
    private String writeTime;
    private Integer approverId;
    private String approvalTime;
}
