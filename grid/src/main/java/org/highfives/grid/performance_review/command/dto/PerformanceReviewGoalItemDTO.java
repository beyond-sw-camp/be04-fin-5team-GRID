package org.highfives.grid.performance_review.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PerformanceReviewGoalItemDTO {
    private Integer id;
    private String jobName;
    private String goal;
    private String metric;
    private Integer weight;
    private String plan;
    private String objection;
    private Integer goalId;

    public PerformanceReviewGoalItemDTO(String jobName, String goal, String metric, int weight, String plan, String objection, int goalId) {
        this.jobName = jobName;
        this.goal = goal;
        this.metric = metric;
        this.weight = weight;
        this.plan = plan;
        this.objection = objection;
        this.goalId = goalId;
    }
}
