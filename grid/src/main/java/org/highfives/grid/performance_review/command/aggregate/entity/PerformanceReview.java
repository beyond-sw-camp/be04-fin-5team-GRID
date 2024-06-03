package org.highfives.grid.performance_review.command.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "performance_review")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PerformanceReview {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type")
    private String type;

    @Column(name = "year")
    private int year;

    @Column(name = "review_name")
    private String reviewName;

    @Column(name = "approval_status")
    @Setter
    private String approvalStatus;

    @Column(name = "writer_id")
    private  int writerId;

    @Column(name = "write_time")
    @Setter
    private  String writeTime;

    @Column(name = "approver_id")
    private int approverId;

    @Column(name = "approval_time")
    @Setter
    private String approvalTime;

    @Column(name = "goal_id")
    private int goalId;

    public PerformanceReview(String type, int year, String reviewName, String approvalStatus, int writerId, String writeTime, int approverId, int goalId) {
        this.type = type;
        this.year = year;
        this.reviewName = reviewName;
        this.approvalStatus = approvalStatus;
        this.writerId = writerId;
        this.writeTime = writeTime;
        this.approverId = approverId;
        this.goalId = goalId;
    }
}
