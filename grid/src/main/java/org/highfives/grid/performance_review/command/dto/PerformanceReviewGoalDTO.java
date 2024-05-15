package org.highfives.grid.performance_review.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PerformanceReviewGoalDTO {
    private int id;
    private int year;
    private String reviewName;
    private String approvalStatus;
    private  int writerId;
    private  String writeTime;
    private int approverId;
    private String approvalTime;

    public PerformanceReviewGoalDTO(int year, String reviewName, String approvalStatus, int writerId, String writeTime, int approverId) {
        this.year = year;
        this.reviewName = reviewName;
        this.approvalStatus = approvalStatus;
        this.writerId = writerId;
        this.writeTime = writeTime;
        this.approverId = approverId;
    }
}
