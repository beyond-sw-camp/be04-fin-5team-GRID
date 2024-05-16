package org.highfives.grid.performance_review.command.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "goal_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PerformanceReviewGoalItem {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "goal")
    private String goal;

    @Column(name = "metric")
    private String metric;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "plan")
    private String plan;

    @Column(name = "objection")
    private String objection;

    @Column(name = "goal_id")
    private Integer goalId;

    public PerformanceReviewGoalItem(String jobName, String goal, String metric, Integer weight, String plan, String objection, Integer goalId) {
        this.jobName = jobName;
        this.goal = goal;
        this.metric = metric;
        this.weight = weight;
        this.plan = plan;
        this.objection = objection;
        this.goalId = goalId;
    }
}
