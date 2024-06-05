package org.highfives.grid.performance_review.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReviewItemDTO {
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
}
