package org.highfives.grid.performance_review.command.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TotalPerformanceReviewDTO {

    private int id;
    private int year;
    private String reviewName;
    private int totalId;
    private double totalScore;
    private int midtermId;
    private int finalId;
    private int revieweeId;
    private int reviewerId;
}
