package org.highfives.grid.performance_review.command.aggregate.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "total_performance_review")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class TotalPerformanceReview {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "year")
    private int year;

    @Column(name = "review_name")
    private String reviewName;

    @Column(name = "total_id")
    private int totalId;

    @Column(name = "total_score")
    private double totalScore;

    @Column(name = "midterm_id")
    private int midtermId;

    @Column(name = "final_id")
    private int finalId;

    @Column(name = "reviewee_id")
    private int revieweeId;

    @Column(name = "reviewer_id")
    private int reviewerId;
}
