package org.highfives.grid.performance_review.command.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "performance_review_item")
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
    @Setter
    private String actionItem;

    @Column(name = "metric")
    private String metric;

    @Column(name = "detail_plan")
    @Setter
    private String detailPlan;

    @Column(name = "weight")
    private int weight;

    @Column(name = "performance")
    @Setter
    private String performance;

    @Column(name = "self_id")
    @Setter
    private int selfId;

    @Column(name = "self_score")
    @Setter
    private double selfScore;

    @Column(name = "self_comment")
    @Setter
    private String selfComment;

    @Column(name = "superior_id")
    @Setter
    private int superiorId;

    @Column(name = "superior_score")
    @Setter
    private double superiorScore;

    @Column(name = "review_id")
    private int reviewId;

    public PerformanceReviewItem(String goal, String actionItem, String metric, String detailPlan, int weight, int selfId, double selfScore, int superiorId, double superiorScore, int reviewId) {
        this.goal = goal;
        this.actionItem = actionItem;
        this.metric = metric;
        this.detailPlan = detailPlan;
        this.weight = weight;
        this.selfId = selfId;
        this.selfScore = selfScore;
        this.superiorId = superiorId;
        this.superiorScore = superiorScore;
        this.reviewId = reviewId;
    }

    //    public PerformanceReviewItem(String goal, String actionItem,String metric, int weight, int selfId, double selfScore, int superiorId, double superiorScore, int reviewId) {
//        this.goal = goal;
//        this.metric = metric;
//        this.weight = weight;
//        this.selfId = selfId;
//        this.selfScore = selfScore;
//        this.superiorId = superiorId;
//        this.superiorScore = superiorScore;
//        this.reviewId = reviewId;
//    }
}
