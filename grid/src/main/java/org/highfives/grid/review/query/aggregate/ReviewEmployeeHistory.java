package org.highfives.grid.review.query.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.highfives.grid.user.command.aggregate.Employee;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewEmployeeHistory {
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
