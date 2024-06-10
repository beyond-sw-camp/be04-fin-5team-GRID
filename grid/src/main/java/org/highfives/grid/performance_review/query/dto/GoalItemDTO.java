package org.highfives.grid.performance_review.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GoalItemDTO {
    private int id;
    private String jobName;
    private String goal;
    private String metric;
    private int weight;
    private String plan;
    private String objection;
}
