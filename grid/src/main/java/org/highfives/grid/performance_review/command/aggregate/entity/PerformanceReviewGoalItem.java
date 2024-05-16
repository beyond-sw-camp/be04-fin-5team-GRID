package org.highfives.grid.performance_review.command.aggregate.entity;

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
    private int id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "goal")
    private String goal;

    @Column(name = "metric")
    private String metric;

    @Column(name = "weight")
    private int weight;

    @Column(name = "plan")
    private String plan;

    @Column(name = "objection")
    private String objection;

    @Column(name = "goal_id")
    private int goalId;

    public PerformanceReviewGoalItem(String jobName, String goal, String metric, Integer weight, String plan, String objection, Integer goalId) {
        this.jobName = jobName;
        this.goal = goal;
        this.metric = metric;
        this.weight = weight;
        this.plan = plan;
        this.objection = objection;
        this.goalId = goalId;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setObjection(String objection) {
        this.objection = objection;
    }
}
