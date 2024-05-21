package org.highfives.grid.performance_review.query.aggregate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
public class PerformanceReviewGoal {
    private int id;
    private int year;
    private String reviewName;
    private String approvalStatus;
    private  int writeId;
    private  String writeTime;
    private int approverId;
    private String approvalTime;

}
