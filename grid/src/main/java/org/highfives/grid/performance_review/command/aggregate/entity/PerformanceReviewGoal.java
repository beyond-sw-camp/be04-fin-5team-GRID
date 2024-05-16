package org.highfives.grid.performance_review.command.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "performance_review_goal")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PerformanceReviewGoal {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
}
