package org.highfives.grid.review.query.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.highfives.grid.review.query.aggregate.Review;
import org.highfives.grid.review.query.aggregate.ReviewStatus;

import java.util.List;


@Getter
@RequiredArgsConstructor
@ToString
@Setter
public class ReviewHistoryAndScoreDTO {

    private int id;

    private String content;

    private ReviewStatus reviewStatus;

    private String writeTime;

    private int year;

    private int quarter;

    private List<ReviewDTO> reviewInfo;


}
