package org.highfives.grid.performance_review.command.aggregate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "review_grade")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ReviewGrade {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "grade")
    private String grade;

    @Column(name = "score")
    private int score;
}
