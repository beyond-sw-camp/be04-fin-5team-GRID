package org.highfives.grid.review.query.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.highfives.grid.review.query.aggregate.ReviewStatus;
import org.highfives.grid.user.command.aggregate.Employee;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString
@Setter
public class ReviewEmployeesHistoryDTO {
    private int id;

    private String content;

    private ReviewStatus reviewStatus;

    private String writeTime;

    private int year;

    private int quarter;

    private int reviewerId;

    private int revieweeId;

    private String revieweeName;

    private String reviewerName;

    private String departmentName;



}
