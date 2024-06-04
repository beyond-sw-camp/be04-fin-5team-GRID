package org.highfives.grid.performance_review.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReviewGradeDTO {
    private int id;
    private String grade;
    private double score;
}
