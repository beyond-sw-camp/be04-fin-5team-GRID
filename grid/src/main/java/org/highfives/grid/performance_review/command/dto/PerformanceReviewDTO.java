package org.highfives.grid.performance_review.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PerformanceReviewDTO {

    private int id;
    private String type;
    private int year;
    private String reviewName;
    private String approvalStatus;
    private  int writerId;
    private  String writeTime;
    private int approverId;
    private String approvalTime;
    private int goalId;

    public PerformanceReviewDTO(String type, int year, String reviewName, int writerId) {
        this.type = type;
        this.year = year;
        this.reviewName = reviewName;
        this.writerId = writerId;
    }
}
