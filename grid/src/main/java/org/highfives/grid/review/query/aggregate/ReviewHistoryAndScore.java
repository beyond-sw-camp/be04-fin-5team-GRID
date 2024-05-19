package org.highfives.grid.review.query.aggregate;

import lombok.*;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewHistoryAndScore {

    private int id;

    private String content;

    private ReviewStatus reviewStatus;

    private String writeTime;

    private int year;

    private int quarter;

    private int reviewerId;

    private int revieweeId;

    private List<Review> reviewInfo;


}
