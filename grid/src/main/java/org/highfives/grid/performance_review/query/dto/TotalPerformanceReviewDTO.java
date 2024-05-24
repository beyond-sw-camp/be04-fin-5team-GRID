package org.highfives.grid.performance_review.query.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TotalPerformanceReviewDTO {

    private int id;
    private int year;
    private String reviewName;
    private int midtermId;
    private int finalId;
    private ReviewGradeDTO totalGrade;
    private WriterDTO reviewee;
    private ApproverDTO reviewer;
}
