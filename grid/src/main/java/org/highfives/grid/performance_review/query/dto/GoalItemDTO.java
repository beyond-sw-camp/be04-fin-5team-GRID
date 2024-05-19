package org.highfives.grid.performance_review.query.dto;

import lombok.Data;

@Data
public class GoalItemDTO {
    private int id;
    private String jobName;
    private String goal;
    private String metric;
    private int weight;
    private String plan;
    private String objection;
}
