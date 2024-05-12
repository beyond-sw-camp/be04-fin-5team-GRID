package org.highfives.grid.review.command.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.highfives.grid.review.command.aggregate.ReviewStatus;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class ReviewHistoryDTO {
    private int id;

    private String content;

    private String reviewStatus;

    private String writeTime;

    private int year;

    private int quarter;

    private int reviewerId;

    private int revieweeId;


}
