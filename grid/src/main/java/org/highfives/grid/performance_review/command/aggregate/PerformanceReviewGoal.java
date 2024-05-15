package org.highfives.grid.performance_review.command.aggregate;

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
    private String approvalStatus;

    @Column(name = "writer_id")
    private  int writerId;

    @Column(name = "write_time")
    private  String writeTime;

    @Column(name = "approver_id")
    private int approverId;

    @Column(name = "approval_time")
    private String approvalTime;
}
