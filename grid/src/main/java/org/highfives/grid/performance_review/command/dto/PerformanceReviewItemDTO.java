package org.highfives.grid.performance_review.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PerformanceReviewItemDTO {

    private int id;
    private String goal;
    private String actionItem;
    private String metric;
    private String detailPlan;
    private int weight;
    private String performance;
    private int selfId;
    private double selfScore;
    private String selfComment;
    private int superiorId;
    private double superiorScore;
    private int reviewId;
}
