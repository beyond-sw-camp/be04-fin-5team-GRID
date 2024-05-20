package org.highfives.grid.performance_review.command.aggregate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "performance_review")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PerformanceReviewItem {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "goal")
    private String goal;

    @Column(name = "action_item")
    private String actionItem;

    @Column(name = "metric")
    private String metric;

    @Column(name = "detail_plan")
    private String detailPlan;

    @Column(name = "weight")
    private int weight;

    @Column(name = "performance")
    private String performance;

    @Column(name = "self_id")
    private int selfId;

    @Column(name = "self_score")
    private float selfScore;

    @Column(name = "self_comment")
    private String selfComment;

    @Column(name = "superior_id")
    private int superiorId;

    @Column(name = "superior_score")
    private float superiorScore;

    @Column(name = "review_id")
    private int reviewId;
}
